package com.expressway.service.impl;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import com.expressway.enumeration.DeviceStatus;
import com.expressway.enumeration.EmeEventStatus;
import com.expressway.mapper.AlarmMessageMapper;
import com.expressway.mapper.EmeEventMapper;
import com.expressway.mapper.SysDeviceMapper;
import com.expressway.service.DashboardService;
import com.expressway.vo.dashboard.DashboardOverviewVO;
import com.expressway.vo.dashboard.WorkbenchOverviewVO;
import com.expressway.vo.dashboard.EventTypeLevelCountVO;
import com.expressway.vo.dashboard.HistogramChartVO;
import com.expressway.vo.dashboard.PieChartDataVO;
import com.expressway.vo.dashboard.StackBarChartVO;
import com.expressway.vo.dashboard.StackBarSeriesVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计看板服务实现
 */
@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private AlarmMessageMapper alarmMessageMapper;

    @Resource
    private SysDeviceMapper sysDeviceMapper;

    @Resource
    private EmeEventMapper emeEventMapper;

    /**
     * 固定的x轴事件类型顺序
     */
    private static final List<DetectEventType> EVENT_TYPE_ORDER = Arrays.asList(
            DetectEventType.CAST,
            DetectEventType.LANDSLIDE,
            DetectEventType.FIRE,
            DetectEventType.TRAFFIC_ACCIDENT
    );

    /**
     * 固定的告警等级顺序
     */
    private static final List<AlarmLevel> ALARM_LEVEL_ORDER = Arrays.asList(
            AlarmLevel.LOW,
            AlarmLevel.MEDIUM,
            AlarmLevel.HIGH,
            AlarmLevel.EMERGENCY
    );

    /**
     * 固定的事件状态顺序
     */
    private static final List<EmeEventStatus> EVENT_STATUS_ORDER = Arrays.asList(
            EmeEventStatus.START,
            EmeEventStatus.CONFIRMED,
            EmeEventStatus.DISPATCHING,
            EmeEventStatus.PROCESSING,
            EmeEventStatus.CLOSED
    );

    /**
     * 固定的设备状态顺序
     */
    private static final List<DeviceStatus> DEVICE_STATUS_ORDER = Arrays.asList(
            DeviceStatus.ONLINE,
            DeviceStatus.OFFLINE,
            DeviceStatus.MAINTENANCE
    );

    @Override
    public DashboardOverviewVO getOverview() {
        DashboardOverviewVO overview = new DashboardOverviewVO();

        // 1. 查询今日总告警数量
        Long todayAlarmCount = alarmMessageMapper.countTodayAlarms();
        overview.setTodayAlarmCount(todayAlarmCount != null ? todayAlarmCount : 0L);

        // 2. 查询设备数
        Long deviceCount = sysDeviceMapper.countAllDevices();
        overview.setDeviceCount(deviceCount != null ? deviceCount : 0L);

        // 3. 查询待处理告警数量
        Long pendingAlarmCount = alarmMessageMapper.countPendingAlarms();
        overview.setPendingAlarmCount(pendingAlarmCount != null ? pendingAlarmCount : 0L);

        // 4. 查询活跃应急事件数量
        Long activeEventCount = emeEventMapper.countActiveEvents();
        overview.setActiveEventCount(activeEventCount != null ? activeEventCount : 0L);

        log.info("实时概览统计: 今日告警={}, 设备数={}, 待处理告警={}, 活跃事件={}",
                overview.getTodayAlarmCount(), overview.getDeviceCount(),
                overview.getPendingAlarmCount(), overview.getActiveEventCount());

        return overview;
    }

    @Override
    public List<PieChartDataVO> getAlarmLevelStats() {
        List<PieChartDataVO> rawData = alarmMessageMapper.countByAlarmLevel();

        // 构建映射表：levelCode -> count
        Map<String, Long> dataMap = new HashMap<>();
        for (PieChartDataVO item : rawData) {
            dataMap.put(item.getName(), item.getValue());
        }

        // 按枚举顺序构建结果，确保所有等级都返回
        List<PieChartDataVO> stats = new ArrayList<>();
        for (AlarmLevel level : ALARM_LEVEL_ORDER) {
            stats.add(new PieChartDataVO(
                    dataMap.getOrDefault(level.getCode(), 0L),
                    level.getDescription()
            ));
        }

        log.info("告警等级统计: 共{}种等级", stats.size());
        return stats;
    }

    @Override
    public StackBarChartVO getEventTypeLevelStats() {
        // 查询数据库
        List<EventTypeLevelCountVO> rawData = alarmMessageMapper.countByEventTypeAndLevel();

        // 构建映射表：(eventType, alarmLevel) -> count
        Map<String, Map<String, Long>> dataMap = new HashMap<>();
        for (EventTypeLevelCountVO item : rawData) {
            dataMap.computeIfAbsent(item.getEventType(), k -> new HashMap<>())
                    .put(item.getAlarmLevel(), item.getCount());
        }

        // 构建x轴（事件类型描述）
        List<String> xAxis = new ArrayList<>();
        for (DetectEventType eventType : EVENT_TYPE_ORDER) {
            xAxis.add(eventType.getDescription());
        }

        // 构建series（每个告警等级一个系列）
        List<StackBarSeriesVO> series = new ArrayList<>();
        for (AlarmLevel level : ALARM_LEVEL_ORDER) {
            List<Long> data = new ArrayList<>();
            for (DetectEventType eventType : EVENT_TYPE_ORDER) {
                Long count = dataMap.getOrDefault(eventType.getCode(), new HashMap<>())
                        .getOrDefault(level.getCode(), 0L);
                data.add(count);
            }
            series.add(new StackBarSeriesVO(level.getDescription(), data));
        }

        StackBarChartVO result = new StackBarChartVO();
        result.setXAxis(xAxis);
        result.setSeries(series);

        log.info("告警危害类型与等级统计完成");
        return result;
    }

    @Override
    public HistogramChartVO getEventStatusStats() {
        // 查询数据库
        List<PieChartDataVO> rawData = emeEventMapper.countByStatus();

        // 构建映射表：status -> count
        Map<String, Long> dataMap = new HashMap<>();
        for (PieChartDataVO item : rawData) {
            dataMap.put(item.getName(), item.getValue());
        }

        // 构建x轴（状态描述）和data
        List<String> xAxis = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        for (EmeEventStatus status : EVENT_STATUS_ORDER) {
            xAxis.add(status.getDescription());
            data.add(dataMap.getOrDefault(status.getCode(), 0L));
        }

        HistogramChartVO result = new HistogramChartVO();
        result.setXAxis(xAxis);
        result.setData(data);

        log.info("事件状态统计完成");
        return result;
    }

    @Override
    public WorkbenchOverviewVO getDeviceOverview() {
        WorkbenchOverviewVO overview = new WorkbenchOverviewVO();

        // 1. 查询设备总数
        Long deviceCount = sysDeviceMapper.countAllDevices();
        overview.setDeviceCount(deviceCount != null ? deviceCount : 0L);

        // 2. 查询在线设备数
        Long onlineDeviceCount = sysDeviceMapper.countOnlineDevices();
        overview.setOnlineDeviceCount(onlineDeviceCount != null ? onlineDeviceCount : 0L);

        // 3. 查询告警总数
        Long alarmCount = alarmMessageMapper.countAllAlarms();
        overview.setAlarmCount(alarmCount != null ? alarmCount : 0L);

        // 4. 查询今日事件数
        Long todayEventCount = emeEventMapper.countTodayEvents();
        overview.setTodayEventCount(todayEventCount != null ? todayEventCount : 0L);

        log.info("设备概览统计: 设备总数={}, 在线设备={}, 告警总数={}, 今日事件={}",
                overview.getDeviceCount(), overview.getOnlineDeviceCount(),
                overview.getAlarmCount(), overview.getTodayEventCount());

        return overview;
    }

    @Override
    public List<PieChartDataVO> getEventTypeStats() {
        List<PieChartDataVO> rawData = emeEventMapper.countByEventType();

        // 构建映射表：eventType -> count
        Map<String, Long> dataMap = new HashMap<>();
        for (PieChartDataVO item : rawData) {
            dataMap.put(item.getName(), item.getValue());
        }

        // 按枚举顺序构建结果，确保所有事件类型都返回
        List<PieChartDataVO> stats = new ArrayList<>();
        for (DetectEventType eventType : EVENT_TYPE_ORDER) {
            stats.add(new PieChartDataVO(
                    dataMap.getOrDefault(eventType.getCode(), 0L),
                    eventType.getDescription()
            ));
        }

        log.info("事件类型统计: 共{}种类型", stats.size());
        return stats;
    }

    @Override
    public HistogramChartVO getDeviceStatusStats() {
        // 查询数据库
        List<PieChartDataVO> rawData = sysDeviceMapper.countByStatus();

        // 构建映射表：status -> count
        Map<String, Long> dataMap = new HashMap<>();
        for (PieChartDataVO item : rawData) {
            dataMap.put(item.getName(), item.getValue());
        }

        // 构建x轴（状态描述）和data
        List<String> xAxis = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        for (DeviceStatus status : DEVICE_STATUS_ORDER) {
            xAxis.add(status.getDescription());
            data.add(dataMap.getOrDefault(status.getCode(), 0L));
        }

        HistogramChartVO result = new HistogramChartVO();
        result.setXAxis(xAxis);
        result.setData(data);

        log.info("设备状态统计完成");
        return result;
    }
}
