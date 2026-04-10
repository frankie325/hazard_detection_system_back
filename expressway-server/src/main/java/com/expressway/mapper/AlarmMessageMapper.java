package com.expressway.mapper;

import com.expressway.dto.AlarmMessageQueryParamsDTO;
import com.expressway.entity.AlarmMessage;
import com.expressway.vo.AlarmMessageVO;
import com.expressway.vo.dashboard.EventTypeLevelCountVO;
import com.expressway.vo.dashboard.PieChartDataVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmMessageMapper {
    /**
     * 分页查询告警消息列表
     */
    List<AlarmMessageVO> selectAlarmMessageList(AlarmMessageQueryParamsDTO queryParams);

    /**
     * 根据ID查询告警消息
     */
    AlarmMessage selectAlarmMessageById(Long id);

    /**
     * 更新告警消息
     */
    int updateAlarmMessageById(AlarmMessage alarmMessage);

    /**
     * 根据ID查询告警消息详情
     */
    AlarmMessageVO selectAlarmMessageVOById(Long id);

    /**
     * 插入告警消息
     */
    int insert(AlarmMessage alarmMessage);

    /**
     * 统计今日告警数量
     */
    Long countTodayAlarms();

    /**
     * 统计待处理告警数量（OPEN状态）
     */
    Long countPendingAlarms();

    /**
     * 按告警等级统计告警数量
     */
    List<PieChartDataVO> countByAlarmLevel();

    /**
     * 按事件类型和告警等级统计告警数量
     */
    List<EventTypeLevelCountVO> countByEventTypeAndLevel();
}
