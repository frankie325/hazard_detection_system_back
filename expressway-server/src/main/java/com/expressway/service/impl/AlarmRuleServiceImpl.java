package com.expressway.service.impl;

import com.expressway.dto.AlarmRuleAddDTO;
import com.expressway.dto.AlarmRuleQueryParamsDTO;
import com.expressway.dto.AlarmRuleUpdateDTO;
import com.expressway.entity.AlarmRule;
import com.expressway.exception.AlarmRuleException;
import com.expressway.mapper.AlarmRuleMapper;
import com.expressway.service.AlarmRuleService;
import com.expressway.vo.AlarmRuleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlarmRuleServiceImpl implements AlarmRuleService {

    @Resource
    private AlarmRuleMapper alarmRuleMapper;

    @Override
    public List<AlarmRuleVO> getAllAlarmRuleList() {
        return alarmRuleMapper.selectAllAlarmRule();
    }

    @Override
    public PageInfo<AlarmRuleVO> getAlarmRuleList(AlarmRuleQueryParamsDTO queryParams) {
        PageHelper.startPage(queryParams.getCurrent(), queryParams.getSize());
        List<AlarmRuleVO> list = alarmRuleMapper.selectAlarmRuleList(queryParams);
        return new PageInfo<>(list);
    }

    @Override
    public AlarmRule getAlarmRuleById(Long id) {
        return alarmRuleMapper.selectAlarmRuleById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAlarmRule(AlarmRuleAddDTO alarmRuleAddDTO) {
        AlarmRule alarmRule = new AlarmRule();
        BeanUtils.copyProperties(alarmRuleAddDTO, alarmRule);
        // JsonNode 直接转字符串
        if (alarmRuleAddDTO.getMatchCondition() != null) {
            alarmRule.setMatchCondition(alarmRuleAddDTO.getMatchCondition().toString());
        }
        int result = alarmRuleMapper.insertAlarmRule(alarmRule);
        if (result != 1) {
            throw new AlarmRuleException("新增告警规则失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAlarmRule(AlarmRuleUpdateDTO alarmRuleUpdateDTO) {
        AlarmRule existRule = alarmRuleMapper.selectAlarmRuleById(alarmRuleUpdateDTO.getId());
        if (existRule == null) {
            throw new AlarmRuleException("告警规则不存在");
        }
        AlarmRule alarmRule = new AlarmRule();
        BeanUtils.copyProperties(alarmRuleUpdateDTO, alarmRule);
        // JsonNode 直接转字符串
        if (alarmRuleUpdateDTO.getMatchCondition() != null) {
            alarmRule.setMatchCondition(alarmRuleUpdateDTO.getMatchCondition().toString());
        }
        int result = alarmRuleMapper.updateAlarmRuleById(alarmRule);
        if (result != 1) {
            throw new AlarmRuleException("更新告警规则失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlarmRuleById(Long id) {
        AlarmRule existRule = alarmRuleMapper.selectAlarmRuleById(id);
        if (existRule == null) {
            throw new AlarmRuleException("告警规则不存在");
        }
        int result = alarmRuleMapper.deleteAlarmRuleById(id);
        if (result != 1) {
            throw new AlarmRuleException("删除告警规则失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteAlarmRule(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new AlarmRuleException("请选择需要删除的数据");
        }
        int result = alarmRuleMapper.batchDeleteAlarmRule(ids);
        if (result == 0) {
            throw new AlarmRuleException("批量删除告警规则失败");
        }
    }
}
