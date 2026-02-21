package com.expressway.controller;

import com.expressway.dto.AlarmRuleAddDTO;
import com.expressway.dto.AlarmRuleQueryParamsDTO;
import com.expressway.dto.AlarmRuleUpdateDTO;
import com.expressway.entity.AlarmRule;
import com.expressway.exception.AlarmRuleException;
import com.expressway.result.Result;
import com.expressway.service.AlarmRuleService;
import com.expressway.vo.AlarmRuleVO;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 告警规则管理控制器
 * 处理告警规则相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/alarm/rule")
public class AlarmRuleController {

    @Resource
    private AlarmRuleService alarmRuleService;

    /**
     * 分页查询告警规则列表
     *
     * @param queryParams 查询参数（可选）
     * @return 告警规则分页数据
     */
    @PostMapping("/list")
    public Result<PageInfo<AlarmRuleVO>> getAlarmRuleList(@Validated @RequestBody AlarmRuleQueryParamsDTO queryParams) {
        try {
            PageInfo<AlarmRuleVO> pageResult = alarmRuleService.getAlarmRuleList(queryParams);
            return Result.success(pageResult);
        } catch (RuntimeException e) {
            return Result.error("查询告警规则列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询单个告警规则（用于编辑回显）
     *
     * @param id 告警规则ID（路径参数）
     * @return 单个告警规则详情
     */
    @GetMapping("/{id}")
    public Result<AlarmRule> getAlarmRuleById(@PathVariable("id") Long id) {
        try {
            AlarmRule alarmRule = alarmRuleService.getAlarmRuleById(id);
            return Result.success(alarmRule);
        } catch (AlarmRuleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询告警规则详情失败：" + e.getMessage());
        }
    }

    /**
     * 新增告警规则
     *
     * @param alarmRuleAddDTO 新增告警规则参数（已做参数校验）
     * @return 统一响应结果
     */
    @PostMapping("/add")
    public Result<?> addAlarmRule(@Validated @RequestBody AlarmRuleAddDTO alarmRuleAddDTO) {
        try {
            alarmRuleService.addAlarmRule(alarmRuleAddDTO);
            return Result.success("新增告警规则成功");
        } catch (AlarmRuleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("新增告警规则失败：" + e.getMessage());
        }
    }

    /**
     * 编辑告警规则
     *
     * @param alarmRuleUpdateDTO 编辑告警规则参数（已做参数校验）
     * @return 统一响应结果
     */
    @PutMapping("/update")
    public Result<?> updateAlarmRule(@Validated @RequestBody AlarmRuleUpdateDTO alarmRuleUpdateDTO) {
        try {
            alarmRuleService.updateAlarmRule(alarmRuleUpdateDTO);
            return Result.success("编辑告警规则成功");
        } catch (AlarmRuleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("编辑告警规则失败：" + e.getMessage());
        }
    }

    /**
     * 单个删除告警规则
     *
     * @param id 告警规则ID（路径参数）
     * @return 统一响应结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteAlarmRuleById(@PathVariable("id") Long id) {
        try {
            alarmRuleService.deleteAlarmRuleById(id);
            return Result.success("删除告警规则成功");
        } catch (AlarmRuleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除告警规则失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除告警规则
     *
     * @param ids 告警规则ID列表（请求体）
     * @return 统一响应结果
     */
    @DeleteMapping("/batchDelete")
    public Result<?> batchDeleteAlarmRule(@RequestBody List<Long> ids) {
        try {
            alarmRuleService.batchDeleteAlarmRule(ids);
            return Result.success("批量删除告警规则成功");
        } catch (AlarmRuleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("批量删除告警规则失败：" + e.getMessage());
        }
    }
}
