package com.expressway.vo.dashboard;

import lombok.Data;

/**
 * 实时概览统计VO
 */
@Data
public class DashboardOverviewVO {
    /**
     * 今日总告警数量
     */
    private Long todayAlarmCount;

    /**
     * 设备总数
     */
    private Long deviceCount;

    /**
     * 待处理告警数量（OPEN状态）
     */
    private Long pendingAlarmCount;

    /**
     * 活跃应急事件数量（DISPATCHING, PROCESSING）
     */
    private Long activeEventCount;
}
