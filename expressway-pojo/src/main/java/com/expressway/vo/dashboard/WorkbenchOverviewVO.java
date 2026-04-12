package com.expressway.vo.dashboard;

import lombok.Data;

/**
 * 设备概览统计VO
 */
@Data
public class WorkbenchOverviewVO {
    /**
     * 监测设备总数
     */
    private Long deviceCount;

    /**
     * 在线设备数
     */
    private Long onlineDeviceCount;

    /**
     * 告警总数
     */
    private Long alarmCount;

    /**
     * 今日事件数
     */
    private Long todayEventCount;
}
