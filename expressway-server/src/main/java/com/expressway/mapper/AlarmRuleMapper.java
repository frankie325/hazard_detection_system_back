package com.expressway.mapper;

import com.expressway.dto.AlarmRuleQueryParamsDTO;
import com.expressway.entity.AlarmRule;
import com.expressway.vo.AlarmRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmRuleMapper {
    /**
     * 分页查询告警规则列表
     */
    List<AlarmRuleVO> selectAlarmRuleList(AlarmRuleQueryParamsDTO queryParams);

    /**
     * 根据ID查询告警规则
     */
    AlarmRule selectAlarmRuleById(Long id);

    /**
     * 新增告警规则
     */
    int insertAlarmRule(AlarmRule alarmRule);

    /**
     * 更新告警规则
     */
    int updateAlarmRuleById(AlarmRule alarmRule);

    /**
     * 根据ID删除告警规则
     */
    int deleteAlarmRuleById(Long id);

    /**
     * 批量删除告警规则
     */
    int batchDeleteAlarmRule(@Param("ids") List<Long> ids);
}
