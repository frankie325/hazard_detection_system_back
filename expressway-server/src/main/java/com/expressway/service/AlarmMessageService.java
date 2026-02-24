package com.expressway.service;

import com.expressway.dto.AlarmConfirmDTO;
import com.expressway.dto.AlarmMessageQueryParamsDTO;
import com.expressway.dto.AlarmMessageUpdateDTO;
import com.expressway.vo.AlarmMessageVO;
import com.github.pagehelper.PageInfo;

public interface AlarmMessageService {
    /**
     * 分页查询告警消息列表
     */
    PageInfo<AlarmMessageVO> getAlarmMessageList(AlarmMessageQueryParamsDTO queryParams);

    /**
     * 修改告警消息
     */
    void updateAlarmMessage(AlarmMessageUpdateDTO updateDTO);

    /**
     * 确认告警
     */
    void confirmAlarmMessage(AlarmConfirmDTO alarmConfirm);

    /**
     * 根据ID查询告警消息详情
     */
    AlarmMessageVO getAlarmMessageById(Long id);
}
