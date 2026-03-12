package com.expressway.service.impl;

import com.expressway.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 仪表盘统计服务实现
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Override
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 设备统计
        statistics.put("device", getDeviceStatusStatistics());
        
        // 告警统计
        statistics.put("alarm", getAlarmStatistics());
        
        // 事件统计
        statistics.put("event", getEventStatistics());
        
        // 趋势数据
        statistics.put("alarmTrend", getAlarmTrend());
        statistics.put("eventTrend", getEventTrend());
        
        return statistics;
    }

    @Override
    public Map<String, Object> getDeviceStatusStatistics() {
        Map<String, Object> deviceStats = new HashMap<>();
        
        // 模拟设备统计数据
        deviceStats.put("total", 150); // 总设备数
        deviceStats.put("online", 135); // 在线设备数
        deviceStats.put("offline", 15); // 离线设备数
        deviceStats.put("warning", 5); // 告警设备数
        
        return deviceStats;
    }

    @Override
    public Map<String, Object> getAlarmStatistics() {
        Map<String, Object> alarmStats = new HashMap<>();
        
        // 模拟告警统计数据
        alarmStats.put("total", 85); // 总告警数
        alarmStats.put("unhandled", 25); // 未处理告警数
        alarmStats.put("processing", 15); // 处理中告警数
        alarmStats.put("closed", 45); // 已关闭告警数
        
        return alarmStats;
    }

    @Override
    public Map<String, Object> getEventStatistics() {
        Map<String, Object> eventStats = new HashMap<>();
        
        // 模拟事件统计数据
        eventStats.put("total", 45); // 总事件数
        eventStats.put("active", 10); // 活跃事件数
        eventStats.put("closed", 35); // 已关闭事件数
        
        return eventStats;
    }

    @Override
    public Map<String, Object> getAlarmTrend() {
        Map<String, Object> trend = new HashMap<>();
        
        // 近7天日期
        List<String> dates = new ArrayList<>();
        // 每天告警数
        List<Integer> counts = new ArrayList<>();
        
        // 模拟近7天数据
        Calendar calendar = Calendar.getInstance();
        for (int i = 6; i >= 0; i--) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date date = calendar.getTime();
            dates.add(new java.text.SimpleDateFormat("MM-dd").format(date));
            counts.add(new Random().nextInt(10) + 5); // 5-14之间的随机数
        }
        
        trend.put("dates", dates);
        trend.put("counts", counts);
        
        return trend;
    }

    @Override
    public Map<String, Object> getEventTrend() {
        Map<String, Object> trend = new HashMap<>();
        
        // 近7天日期
        List<String> dates = new ArrayList<>();
        // 每天事件数
        List<Integer> counts = new ArrayList<>();
        
        // 模拟近7天数据
        Calendar calendar = Calendar.getInstance();
        for (int i = 6; i >= 0; i--) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date date = calendar.getTime();
            dates.add(new java.text.SimpleDateFormat("MM-dd").format(date));
            counts.add(new Random().nextInt(5) + 2); // 2-6之间的随机数
        }
        
        trend.put("dates", dates);
        trend.put("counts", counts);
        
        return trend;
    }
}
