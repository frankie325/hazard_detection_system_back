package com.expressway.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.expressway.config.PythonDetectConfig;
import com.expressway.entity.AlarmMessage;
import com.expressway.entity.AlarmRule;
import com.expressway.entity.DetectEventStream;
import com.expressway.entity.SysDevice;
import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.AlarmStatus;
import com.expressway.enumeration.DetectEventType;
import com.expressway.handler.AlarmWebSocketHandler;
import com.expressway.service.AlarmMessageService;
import com.expressway.service.AlarmRuleService;
import com.expressway.service.DetectEventStreamService;
import com.expressway.service.SysDeviceService;
import com.expressway.service.VideoDetectionService;
import com.expressway.vo.AlarmMessageVO;
import com.expressway.vo.AlarmRuleVO;
import com.expressway.vo.FrameResultVO;
import com.expressway.vo.TrackInfoVO;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 视频检测服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoDetectionServiceImpl implements VideoDetectionService {

    private static final String MJPEG_HEADER = "--frame\r\nContent-Type: image/jpeg\r\n\r\n";
    private static final String MJPEG_FOOTER = "\r\n";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final PythonDetectConfig config;
    private final DetectEventStreamService eventStreamService;
    private final AlarmMessageService alarmMessageService;
    private final AlarmRuleService alarmRuleService;
    private final SysDeviceService deviceService;
    private final AlarmWebSocketHandler alarmWebSocketHandler;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    private final Map<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();
    private final Map<String, Long> lastEventStreamTimeMap = new ConcurrentHashMap<>(); // 会话最后事件流时间
    private final Map<String, Set<Integer>> alarmedTracksMap = new ConcurrentHashMap<>(); // 已告警的trackId

    @PreDestroy
    public void destroy() {
        webSocketMap.values().forEach(ws -> {
            try {
                ws.close(1000, "Service shutdown");
            } catch (Exception e) {
                log.debug("关闭WebSocket连接失败: {}", e.getMessage());
            }
        });
        webSocketMap.clear();
        lastEventStreamTimeMap.clear();
        alarmedTracksMap.clear();
        httpClient.dispatcher().executorService().shutdown();
    }

    @Override
    public void startMjpegStream(Long deviceId, String videoUrl, OutputStream outputStream) {
        String sessionId = null;

        try {
            // 1. 启动Python检测会话
            sessionId = startPythonSession(deviceId, videoUrl);
            log.info("Python检测会话已启动: sessionId={}, deviceId={}", sessionId, deviceId);

            String finalSessionId = sessionId;

            // 初始化会话状态
            lastEventStreamTimeMap.put(sessionId, 0L);
            alarmedTracksMap.put(sessionId, ConcurrentHashMap.newKeySet());

            // 2. 建立WebSocket连接并传输MJPEG流
            String wsEndpoint = config.getWsDetectUrl(sessionId);

            Request request = new Request.Builder()
                    .url(wsEndpoint)
                    .build();

            WebSocketListener listener = new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    log.info("WebSocket连接已建立: sessionId={}", finalSessionId);
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    try {
                        // 解析帧检测结果
                        FrameResultVO frameResult = JSON.parseObject(text, FrameResultVO.class);
                        handleFrameResult(frameResult, deviceId, finalSessionId);
                    } catch (Exception e) {
                        log.error("处理帧检测结果失败: {}", e.getMessage());
                    }
                }

                @Override
                public void onMessage(WebSocket webSocket, okio.ByteString bytes) {
                    try {
                        // 写入MJPEG帧
                        byte[] jpegData = bytes.toByteArray();
                        outputStream.write(MJPEG_HEADER.getBytes());
                        outputStream.write(jpegData);
                        outputStream.write(MJPEG_FOOTER.getBytes());
                        outputStream.flush();
                    } catch (IOException e) {
                        log.debug("写入MJPEG帧失败，客户端可能已断开: {}", e.getMessage());
                        // 立即从map中移除，让主循环退出，不再接收新消息
                        webSocketMap.remove(finalSessionId);
                        webSocket.close(1000, "Client disconnected");
                    }
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    log.error("WebSocket连接失败: sessionId={}, error={}", finalSessionId, t.getMessage());
                    webSocketMap.remove(finalSessionId);
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    log.info("WebSocket正在关闭: sessionId={}, code={}, reason={}", finalSessionId, code, reason);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    log.info("WebSocket已关闭: sessionId={}, code={}, reason={}", finalSessionId, code, reason);
                    webSocketMap.remove(finalSessionId);
                    cleanupSession(finalSessionId);
                }
            };

            WebSocket webSocket = httpClient.newWebSocket(request, listener);
            webSocketMap.put(sessionId, webSocket);

            // 3. 阻塞等待WebSocket关闭
            while (webSocketMap.containsKey(sessionId)) {
                Thread.sleep(100);
            }

        } catch (InterruptedException e) {
            log.info("MJPEG流传输被中断: sessionId={}", sessionId);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("MJPEG流传输异常: {}", e.getMessage(), e);
        } finally {
            // 4. 清理资源
            if (sessionId != null) {
                stopPythonSession(sessionId);
                WebSocket ws = webSocketMap.remove(sessionId);
                if (ws != null) {
                    ws.close(1000, "Stream ended");
                }
                cleanupSession(sessionId);
            }
            log.info("MJPEG流传输结束: sessionId={}", sessionId);
        }
    }

    /**
     * 处理帧检测结果
     */
    private void handleFrameResult(FrameResultVO frameResult, Long deviceId, String sessionId) {
        if (frameResult == null || frameResult.getTracks() == null) {
            return;
        }

        List<TrackInfoVO> tracks = frameResult.getTracks();
        long currentTime = System.currentTimeMillis();

        // 1. 事件流采样 - 按间隔生成事件流
        Long lastTime = lastEventStreamTimeMap.getOrDefault(sessionId, 0L);
        int interval = config.getEventStreamInterval() * 1000; // 转换为毫秒

        if (currentTime - lastTime >= interval) {
            for (TrackInfoVO track : tracks) {
                createEventStream(track, deviceId);
            }
            lastEventStreamTimeMap.put(sessionId, currentTime);
        }

        // 2. 实时告警检查
        checkAlarms(tracks, deviceId, sessionId);
    }

    /**
     * 创建事件流
     */
    private void createEventStream(TrackInfoVO track, Long deviceId) {
        DetectEventType eventType = track.getType();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String eventName = eventType.getDescription() + "-" + track.getClassName() + "-" + timestamp;

        // 获取设备信息
        // SysDevice device = deviceService.getDeviceById(deviceId);

        DetectEventStream eventStream = new DetectEventStream();
        eventStream.setEventName(eventName);
        eventStream.setDeviceId(deviceId);
        eventStream.setEventType(eventType);
        eventStream.setConfidence(track.getConfidence() != null ? track.getConfidence().floatValue() : null);

        eventStreamService.createEventStream(eventStream);
        log.debug("创建事件流: eventName={}, deviceId={}, className={}", eventName, deviceId, track.getClassName());
    }

    /**
     * 检查告警规则
     */
    private void checkAlarms(List<TrackInfoVO> tracks, Long deviceId, String sessionId) {
        Set<Integer> alarmedTracks = alarmedTracksMap.get(sessionId);
        if (alarmedTracks == null) {
            return;
        }

        // 获取所有启用的告警规则
        List<AlarmRuleVO> rules = alarmRuleService.getAllAlarmRuleList();
        if (rules == null || rules.isEmpty()) {
            return;
        }

        for (TrackInfoVO track : tracks) {
            // 跳过已告警的track
            if (alarmedTracks.contains(track.getTrackId())) {
                continue;
            }

            for (AlarmRuleVO rule : rules) {
                if (rule.getIsEnabled() == null || rule.getIsEnabled() != 1) {
                    continue;
                }

                // 每个检测跟踪物体和告警规则进行匹配，应用告警规则的危害类型
                DetectEventType trackEventType = track.getType();
                if (rule.getEventType() != trackEventType) {
                    continue;
                }

                // 解析匹配条件
                if (matchCondition(track, rule)) {
                    createAlarm(track, deviceId, rule);
                    alarmedTracks.add(track.getTrackId());
                    break; // 每个track只触发一个告警
                }
            }
        }
    }

    /**
     * 匹配告警规则条件
     */
    private boolean matchCondition(TrackInfoVO track, AlarmRuleVO rule) {
        if (rule.getMatchCondition() == null || rule.getMatchCondition().isEmpty()) {
            return false;
        }

        try {
            JSONObject condition = JSON.parseObject(rule.getMatchCondition());

            // 检查置信度
            JSONObject confidence = condition.getJSONObject("confidence");
            if (confidence != null) {
                Double minConfidence = confidence.getDouble("min");
                if (minConfidence != null && track.getConfidence() != null) {
                    if (track.getConfidence() < minConfidence) {
                        return false;
                    }
                }
            }

            // 检查持续时间
            JSONObject duration = condition.getJSONObject("duration_seconds");
            if (duration != null) {
                Double gtDuration = duration.getDouble("gt");
                if (gtDuration != null && track.getDurationSeconds() != null) {
                    if (track.getDurationSeconds() <= gtDuration) {
                        return false;
                    }
                }
            }

            return true;
        } catch (Exception e) {
            log.error("解析告警规则条件失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 创建告警消息
     */
    private void createAlarm(TrackInfoVO track, Long deviceId, AlarmRuleVO rule) {
        DetectEventType eventType = track.getType();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String alarmName = eventType.getDescription() + "告警-" + track.getClassName() + "-" + timestamp;

        AlarmMessage alarmMessage = new AlarmMessage();
        alarmMessage.setAlarmName(alarmName);
        alarmMessage.setAlarmLevel(rule.getAlarmLevel() != null ? rule.getAlarmLevel() : AlarmLevel.MEDIUM);
        alarmMessage.setDeviceId(deviceId);
        alarmMessage.setEventType(eventType);
        alarmMessage.setRuleId(rule.getId());
        alarmMessage.setAlarmStatus(AlarmStatus.OPEN);

        // 创建告警并获取ID
        AlarmMessage createdAlarm = alarmMessageService.createAlarmMessage(alarmMessage);
        log.info("创建告警: alarmName={}, deviceId={}, className={}", alarmName, deviceId, track.getClassName());

        // 通过WebSocket推送告警消息（查询完整的关联信息）
        AlarmMessageVO alarmMessageVO = alarmMessageService.getAlarmMessageById(createdAlarm.getId());
        alarmWebSocketHandler.pushAlarm(alarmMessageVO);
    }

    /**
     * 清理会话状态
     */
    private void cleanupSession(String sessionId) {
        lastEventStreamTimeMap.remove(sessionId);
        alarmedTracksMap.remove(sessionId);
    }

    /**
     * 启动Python检测会话
     */
    private String startPythonSession(Long deviceId, String videoUrl) throws IOException {
        String url = config.getSessionStartUrl();

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("device_id", deviceId);
        bodyJson.put("video_url", videoUrl);

        RequestBody body = RequestBody.create(bodyJson.toJSONString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("启动检测会话失败: HTTP " + response.code());
            }
            String responseBody = response.body().string();
            JSONObject result = JSON.parseObject(responseBody);
            return result.getString("session_id");
        }
    }

    /**
     * 停止Python检测会话
     */
    private void stopPythonSession(String sessionId) {
        if (sessionId == null) return;

        try {
            String url = config.getSessionStopUrl(sessionId);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create("", MediaType.parse("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    log.info("Python检测会话已停止: sessionId={}", sessionId);
                } else {
                    log.warn("停止Python检测会话失败: sessionId={}, code={}", sessionId, response.code());
                }
            }
        } catch (Exception e) {
            log.error("停止Python检测会话异常: sessionId={}, error={}", sessionId, e.getMessage());
        }
    }

    @Override
    public String getPythonServiceUrl() {
        return config.getUrl();
    }
}
