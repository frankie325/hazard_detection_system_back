package com.expressway.vo.dashboard;

import lombok.Data;

/**
 * 事件类型与告警等级统计VO（用于Mapper返回）
 */
@Data
public class EventTypeLevelCountVO {
    /**
     * 事件类型code
     */
    private String eventType;

    /**
     * 告警等级code
     */
    private String alarmLevel;

    /**
     * 数量
     */
    private Long count;
}
