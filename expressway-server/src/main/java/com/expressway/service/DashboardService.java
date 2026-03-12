package com.expressway.service;

import java.util.Map;

/**
 * 仪表盘统计服务
 * 提供系统数据统计功能，用于仪表盘展示
 */
public interface DashboardService {
    /**
     * 获取仪表盘统计数据
     */
    Map<String, Object> getDashboardStatistics();

    /**
     * 获取设备状态统计
     */
    Map<String, Object> getDeviceStatusStatistics();

    /**
     * 获取告警统计
     */
    Map<String, Object> getAlarmStatistics();

    /**
     * 获取事件统计
     */
    Map<String, Object> getEventStatistics();

    /**
     * 获取近7天告警趋势
     */
    Map<String, Object> getAlarmTrend();

    /**
     * 获取近7天事件趋势
     */
    Map<String, Object> getEventTrend();
}
