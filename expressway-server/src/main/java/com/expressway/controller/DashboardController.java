package com.expressway.controller;

import com.expressway.result.Result;
import com.expressway.service.DashboardService;
import com.expressway.vo.dashboard.DashboardOverviewVO;
import com.expressway.vo.dashboard.HistogramChartVO;
import com.expressway.vo.dashboard.PieChartDataVO;
import com.expressway.vo.dashboard.StackBarChartVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计看板控制器
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 获取实时概览统计数据
     */
    @GetMapping("/overview")
    public Result<DashboardOverviewVO> getOverview() {
        DashboardOverviewVO overview = dashboardService.getOverview();
        return Result.success(overview);
    }

    /**
     * 获取告警等级统计数据
     */
    @GetMapping("/alarm/level")
    public Result<List<PieChartDataVO>> getAlarmLevelStats() {
        List<PieChartDataVO> stats = dashboardService.getAlarmLevelStats();
        return Result.success(stats);
    }

    /**
     * 获取告警危害类型统计，使用堆叠柱状图展示告警等级分布
     */
    @GetMapping("/alarm/eventTypeLevel")
    public Result<StackBarChartVO> getEventTypeLevelStats() {
        StackBarChartVO stats = dashboardService.getEventTypeLevelStats();
        return Result.success(stats);
    }

    /**
     * 获取事件状态统计直方图
     */
    @GetMapping("/event/status")
    public Result<HistogramChartVO> getEventStatusStats() {
        HistogramChartVO stats = dashboardService.getEventStatusStats();
        return Result.success(stats);
    }
}
