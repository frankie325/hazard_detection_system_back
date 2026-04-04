package com.expressway.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * WebSocket帧检测结果VO
 */
@Data
public class FrameResultVO {
    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 帧号
     */
    private Integer frameNo;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 视频FPS
     */
    private Double videoFps;

    /**
     * 检测持续时间(秒)
     */
    private Double durationSeconds;

    /**
     * 跟踪目标列表
     */
    private List<TrackInfoVO> tracks;

    /**
     * 统计信息
     */
    private Map<String, Object> stats;

    /**
     * 事件列表
     */
    private List<DetectionEventVO> events;
}
