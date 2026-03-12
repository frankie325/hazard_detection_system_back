package com.expressway.controller;

import com.expressway.result.Result;
import com.expressway.service.DashboardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统管理控制器
 * 处理系统相关的HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 系统健康检查
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> healthCheck() {
        try {
            // 构建健康检查结果
            Map<String, Object> healthInfo = new HashMap<>();
            healthInfo.put("status", "UP");
            healthInfo.put("timestamp", System.currentTimeMillis());
            healthInfo.put("message", "系统运行正常");
            
            // 系统信息
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("javaVersion", System.getProperty("java.version"));
            systemInfo.put("osName", System.getProperty("os.name"));
            systemInfo.put("osVersion", System.getProperty("os.version"));
            systemInfo.put("serverTime", System.currentTimeMillis());
            
            healthInfo.put("system", systemInfo);
            
            return Result.success(healthInfo);
        } catch (Exception e) {
            return Result.error("健康检查失败：" + e.getMessage());
        }
    }

    /**
     * 获取系统版本信息
     */
    @GetMapping("/version")
    public Result<Map<String, Object>> getVersion() {
        try {
            Map<String, Object> versionInfo = new HashMap<>();
            versionInfo.put("version", "1.0.0");
            versionInfo.put("name", "expressway-hazard-detection-system");
            versionInfo.put("description", "高速公路危险检测系统");
            versionInfo.put("buildTime", "2024-01-01");
            
            return Result.success(versionInfo);
        } catch (Exception e) {
            return Result.error("获取版本信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardStatistics() {
        try {
            Map<String, Object> statistics = dashboardService.getDashboardStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取仪表盘统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备状态统计
     */
    @GetMapping("/dashboard/device")
    public Result<Map<String, Object>> getDeviceStatusStatistics() {
        try {
            Map<String, Object> deviceStats = dashboardService.getDeviceStatusStatistics();
            return Result.success(deviceStats);
        } catch (Exception e) {
            return Result.error("获取设备状态统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取告警统计
     */
    @GetMapping("/dashboard/alarm")
    public Result<Map<String, Object>> getAlarmStatistics() {
        try {
            Map<String, Object> alarmStats = dashboardService.getAlarmStatistics();
            return Result.success(alarmStats);
        } catch (Exception e) {
            return Result.error("获取告警统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取事件统计
     */
    @GetMapping("/dashboard/event")
    public Result<Map<String, Object>> getEventStatistics() {
        try {
            Map<String, Object> eventStats = dashboardService.getEventStatistics();
            return Result.success(eventStats);
        } catch (Exception e) {
            return Result.error("获取事件统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取近7天告警趋势
     */
    @GetMapping("/dashboard/alarm/trend")
    public Result<Map<String, Object>> getAlarmTrend() {
        try {
            Map<String, Object> trend = dashboardService.getAlarmTrend();
            return Result.success(trend);
        } catch (Exception e) {
            return Result.error("获取告警趋势失败：" + e.getMessage());
        }
    }

    /**
     * 获取近7天事件趋势
     */
    @GetMapping("/dashboard/event/trend")
    public Result<Map<String, Object>> getEventTrend() {
        try {
            Map<String, Object> trend = dashboardService.getEventTrend();
            return Result.success(trend);
        } catch (Exception e) {
            return Result.error("获取事件趋势失败：" + e.getMessage());
        }
    }
}
