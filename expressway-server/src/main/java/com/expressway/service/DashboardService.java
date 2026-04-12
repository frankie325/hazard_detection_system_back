package com.expressway.service;

import com.expressway.vo.dashboard.DashboardOverviewVO;
import com.expressway.vo.dashboard.WorkbenchOverviewVO;
import com.expressway.vo.dashboard.HistogramChartVO;
import com.expressway.vo.dashboard.PieChartDataVO;
import com.expressway.vo.dashboard.StackBarChartVO;

import java.util.List;

/**
 * 统计看板服务接口
 */
public interface DashboardService {
    /**
     * 获取实时概览统计数据
     */
    DashboardOverviewVO getOverview();

    /**
     * 获取告警等级统计数据
     */
    List<PieChartDataVO> getAlarmLevelStats();

    /**
     * 获取告警危害类型与等级统计（堆叠柱状图）
     */
    StackBarChartVO getEventTypeLevelStats();

    /**
     * 获取事件状态统计直方图
     */
    HistogramChartVO getEventStatusStats();

    /**
     * 获取设备概览统计数据
     */
    WorkbenchOverviewVO getDeviceOverview();

    /**
     * 获取事件类型统计（环形图）
     */
    List<PieChartDataVO> getEventTypeStats();

    /**
     * 获取设备状态统计（柱状图）
     */
    HistogramChartVO getDeviceStatusStats();
}
