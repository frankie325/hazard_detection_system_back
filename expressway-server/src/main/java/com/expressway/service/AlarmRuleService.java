package com.expressway.service;

import com.expressway.dto.AlarmRuleAddDTO;
import com.expressway.dto.AlarmRuleQueryParamsDTO;
import com.expressway.dto.AlarmRuleUpdateDTO;
import com.expressway.entity.AlarmRule;
import com.expressway.vo.AlarmRuleVO;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AlarmRuleService {
    /**
     * 查询所有告警规则列表（不分页）
     */
    List<AlarmRuleVO> getAllAlarmRuleList();

    /**
     * 分页查询告警规则列表
     */
    PageInfo<AlarmRuleVO> getAlarmRuleList(AlarmRuleQueryParamsDTO queryParams);

    /**
     * 根据ID查询告警规则
     */
    AlarmRule getAlarmRuleById(Long id);

    /**
     * 新增告警规则
     */
    void addAlarmRule(AlarmRuleAddDTO alarmRuleAddDTO);

    /**
     * 更新告警规则
     */
    void updateAlarmRule(AlarmRuleUpdateDTO alarmRuleUpdateDTO);

    /**
     * 根据ID删除告警规则
     */
    void deleteAlarmRuleById(Long id);

    /**
     * 批量删除告警规则
     */
    void batchDeleteAlarmRule(List<Long> ids);

    /**
     * 导出告警规则为JSON文件
     */
    void exportAlarmRules(HttpServletResponse response) throws IOException;

    /**
     * 从JSON文件导入告警规则
     */
    void importAlarmRules(MultipartFile file) throws IOException;
}
