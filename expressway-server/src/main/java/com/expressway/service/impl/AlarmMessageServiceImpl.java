package com.expressway.service.impl;

import com.expressway.dto.AlarmConfirmDTO;
import com.expressway.dto.AlarmMessageQueryParamsDTO;
import com.expressway.dto.AlarmMessageUpdateDTO;
import com.expressway.dto.EmeEventAddDTO;
import com.expressway.entity.AlarmMessage;
import com.expressway.enumeration.AlarmStatus;
import com.expressway.exception.AlarmMessageException;
import com.expressway.mapper.AlarmMessageMapper;
import com.expressway.service.AlarmMessageService;
import com.expressway.service.EmeEventService;
import com.expressway.vo.AlarmMessageVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AlarmMessageServiceImpl implements AlarmMessageService {

    @Resource
    private AlarmMessageMapper alarmMessageMapper;
    @Autowired
    private EmeEventService emergencyEventService;

    @Override
    public PageInfo<AlarmMessageVO> getAlarmMessageList(AlarmMessageQueryParamsDTO queryParams) {
        PageHelper.startPage(queryParams.getCurrent(), queryParams.getSize());
        List<AlarmMessageVO> list = alarmMessageMapper.selectAlarmMessageList(queryParams);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAlarmMessage(AlarmMessageUpdateDTO updateDTO) {
        AlarmMessage existMessage = alarmMessageMapper.selectAlarmMessageById(updateDTO.getId());
        if (existMessage == null) {
            throw new AlarmMessageException("告警消息不存在");
        }
        AlarmMessage alarmMessage = new AlarmMessage();
        BeanUtils.copyProperties(updateDTO, alarmMessage);
        int result = alarmMessageMapper.updateAlarmMessageById(alarmMessage);
        if (result != 1) {
            throw new AlarmMessageException("修改告警消息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmAlarmMessage(AlarmConfirmDTO alarmConfirm) {
        // 1.检查告警消息是否存在
        AlarmMessage existMessage = alarmMessageMapper.selectAlarmMessageById(alarmConfirm.getAlarmId());
        if (existMessage == null) {
            throw new AlarmMessageException("告警消息不存在");
        }

        // 2.检查告警状态，已关闭的告警不能重复确认
        if (existMessage.getAlarmStatus() == AlarmStatus.CLOSED) {
            throw new AlarmMessageException("告警已关闭，无法重复确认");
        }

        // 3.检查告警状态，处理中的告警不能重复确认
        if (existMessage.getAlarmStatus() == AlarmStatus.PROCESSING) {
            throw new AlarmMessageException("告警正在处理中，请勿重复确认");
        }

        // 4.修改告警状态为处理中
        AlarmMessageUpdateDTO updateDTO = new AlarmMessageUpdateDTO();
        updateDTO.setId(alarmConfirm.getAlarmId());
        updateDTO.setAlarmStatus(AlarmStatus.PROCESSING);
        updateAlarmMessage(updateDTO);

        // 5.获取告警详情
        AlarmMessageVO alarmMessage = getAlarmMessageById(alarmConfirm.getAlarmId());

        // 6.生成应急事件
        EmeEventAddDTO emeEventAddDTO = new EmeEventAddDTO();
        // 生成规则为${类型}事件-${等级}-年月日时分秒时间戳
        // 年月日时分秒时间戳使用LocalDateTime去生成
        emeEventAddDTO.setEventName(alarmMessage.getEventTypeName() + "事件-" + alarmMessage.getAlarmLevelName() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        emeEventAddDTO.setEventType(alarmMessage.getEventType());
        emeEventAddDTO.setEventLevel(alarmMessage.getAlarmLevel());
        emeEventAddDTO.setLocation(alarmMessage.getLocation());
        emeEventAddDTO.setDeptId(alarmConfirm.getDeptId());
        emeEventAddDTO.setAlarmId(alarmMessage.getId());
        emergencyEventService.createEmeEvent(emeEventAddDTO);
    }

    @Override
    public AlarmMessageVO getAlarmMessageById(Long id) {
        AlarmMessageVO alarmMessageVO = alarmMessageMapper.selectAlarmMessageVOById(id);
        if (alarmMessageVO == null) {
            throw new AlarmMessageException("告警消息不存在");
        }
        return alarmMessageVO;
    }
}
