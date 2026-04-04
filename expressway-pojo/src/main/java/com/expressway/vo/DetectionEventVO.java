package com.expressway.vo;

import lombok.Data;

/**
 * 检测事件VO
 */
@Data
public class DetectionEventVO {
    /**
     * 跟踪ID
     */
    private Integer trackId;

    /**
     * 事件类型：appear, disappear
     */
    private String eventType;

    /**
     * 类别名称
     */
    private String className;

    /**
     * 置信度
     */
    private Double confidence;

    /**
     * 总持续时长（秒），disappear事件时有值
     */
    private Double totalDurationSeconds;

    /**
     * 事件时间戳
     */
    private String timestamp;
}
