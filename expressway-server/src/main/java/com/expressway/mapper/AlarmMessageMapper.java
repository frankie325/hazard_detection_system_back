package com.expressway.mapper;

import com.expressway.dto.AlarmMessageQueryParamsDTO;
import com.expressway.entity.AlarmMessage;
import com.expressway.vo.AlarmMessageVO;
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
}
