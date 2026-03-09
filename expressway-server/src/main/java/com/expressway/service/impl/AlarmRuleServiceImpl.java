package com.expressway.service.impl;

import com.expressway.dto.AlarmRuleAddDTO;
import com.expressway.dto.AlarmRuleQueryParamsDTO;
import com.expressway.dto.AlarmRuleUpdateDTO;
import com.expressway.entity.AlarmRule;
import com.expressway.exception.AlarmRuleException;
import com.expressway.mapper.AlarmRuleMapper;
import com.expressway.service.AlarmRuleService;
import com.expressway.vo.AlarmRuleVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Override
    public void exportAlarmRules(HttpServletResponse response) throws IOException {
        // 查询所有告警规则
        List<AlarmRuleVO> ruleList = alarmRuleMapper.selectAllAlarmRule();

        // 配置ObjectMapper以支持Java 8日期时间
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 将规则列表转换为JSON字符串
        String jsonContent = mapper.writeValueAsString(ruleList);

        // 设置响应头
        String fileName = URLEncoder.encode("alarm_rules_" + System.currentTimeMillis() + ".json", StandardCharsets.UTF_8);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 写入响应流
        response.getWriter().write(jsonContent);
        response.getWriter().flush();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importAlarmRules(MultipartFile file) throws IOException {
        // 配置ObjectMapper以支持Java 8日期时间
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // 读取JSON文件并转换为规则列表
        List<AlarmRule> ruleList = mapper.readValue(file.getInputStream(), new TypeReference<List<AlarmRule>>() {});

        if (ruleList == null || ruleList.isEmpty()) {
            throw new AlarmRuleException("导入的文件中没有有效的告警规则数据");
        }

        // 遍历并导入每个规则
        for (AlarmRule rule : ruleList) {
            // 清除ID，让数据库自动生成
            rule.setId(null);
            // 设置默认启用状态
            if (rule.getIsEnabled() == null) {
                rule.setIsEnabled(1);
            }

            int result = alarmRuleMapper.insertAlarmRule(rule);
            if (result != 1) {
                throw new AlarmRuleException("导入告警规则失败：" + rule.getRuleName());
            }
        }
    }
}
