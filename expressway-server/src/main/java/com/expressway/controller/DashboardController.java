package com.expressway.controller;

import com.expressway.result.Result;
import com.expressway.service.DashboardService;
import com.expressway.vo.dashboard.DashboardOverviewVO;
import com.expressway.vo.dashboard.WorkbenchOverviewVO;
import com.expressway.vo.dashboard.HistogramChartVO;
import com.expressway.vo.dashboard.PieChartDataVO;
import com.expressway.vo.dashboard.StackBarChartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "统计看板接口")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 获取实时概览统计数据
     */
    @GetMapping("/overview")
    @ApiOperation("实时概览统计")
    public Result<DashboardOverviewVO> getOverview() {
        DashboardOverviewVO overview = dashboardService.getOverview();
        return Result.success(overview);
    }

    /**
     * 获取告警等级统计数据
     */
    @GetMapping("/alarm/level")
    @ApiOperation("告警等级统计-环形图")
    public Result<List<PieChartDataVO>> getAlarmLevelStats() {
        List<PieChartDataVO> stats = dashboardService.getAlarmLevelStats();
        return Result.success(stats);
    }

    /**
     * 获取告警危害类型统计，使用堆叠柱状图展示告警等级分布
     */
    @GetMapping("/alarm/eventTypeLevel")
    @ApiOperation("告警危害类型与等级统计-堆叠柱状图")
    public Result<StackBarChartVO> getEventTypeLevelStats() {
        StackBarChartVO stats = dashboardService.getEventTypeLevelStats();
        return Result.success(stats);
    }

    /**
     * 获取事件状态统计直方图
     */
    @GetMapping("/event/status")
    @ApiOperation("事件状态统计-直方图")
    public Result<HistogramChartVO> getEventStatusStats() {
        HistogramChartVO stats = dashboardService.getEventStatusStats();
        return Result.success(stats);
    }

    /**
     * 获取设备概览统计数据
     */
    @GetMapping("/workbench/overview")
    @ApiOperation("工作台概览统计")
    public Result<WorkbenchOverviewVO> getDeviceOverview() {
        WorkbenchOverviewVO overview = dashboardService.getDeviceOverview();
        return Result.success(overview);
    }

    /**
     * 获取事件类型统计（环形图）
     */
    @GetMapping("/event/type")
    @ApiOperation("事件类型统计-环形图")
    public Result<List<PieChartDataVO>> getEventTypeStats() {
        List<PieChartDataVO> stats = dashboardService.getEventTypeStats();
        return Result.success(stats);
    }

    /**
     * 获取设备状态统计（柱状图）
     */
    @GetMapping("/device/status")
    @ApiOperation("设备状态统计-柱状图")
    public Result<HistogramChartVO> getDeviceStatusStats() {
        HistogramChartVO stats = dashboardService.getDeviceStatusStats();
        return Result.success(stats);
    }
}
