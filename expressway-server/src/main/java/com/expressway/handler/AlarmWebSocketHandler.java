package com.expressway.handler;

import com.alibaba.fastjson.JSON;
import com.expressway.vo.AlarmMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 告警WebSocket处理器
 * 负责向前端推送告警消息
 */
@Slf4j
@Component
public class AlarmWebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("WebSocket连接建立: sessionId={}, 当前连接数={}", session.getId(), sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("WebSocket连接关闭: sessionId={}, status={}, 当前连接数={}", session.getId(), status, sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理客户端发送的消息（如需要心跳检测可在此处理）
        log.debug("收到WebSocket消息: sessionId={}, payload={}", session.getId(), message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误: sessionId={}, error={}", session.getId(), exception.getMessage());
        sessions.remove(session);
    }

    /**
     * 向所有连接的客户端推送告警消息
     */
    public void pushAlarm(AlarmMessageVO alarmMessageVO) {
        if (sessions.isEmpty()) {
            log.debug("没有WebSocket连接，跳过告警推送");
            return;
        }

        String message = JSON.toJSONString(alarmMessageVO);
        TextMessage textMessage = new TextMessage(message);

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                    log.debug("告警推送成功: sessionId={}, alarmId={}", session.getId(), alarmMessageVO.getId());
                } catch (IOException e) {
                    log.error("告警推送失败: sessionId={}, error={}", session.getId(), e.getMessage());
                }
            }
        }
    }
}
