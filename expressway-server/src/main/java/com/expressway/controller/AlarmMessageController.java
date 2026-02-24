package com.expressway.controller;

import com.expressway.dto.AlarmConfirmDTO;
import com.expressway.dto.AlarmMessageQueryParamsDTO;
import com.expressway.dto.AlarmMessageUpdateDTO;
import com.expressway.exception.AlarmMessageException;
import com.expressway.result.Result;
import com.expressway.service.AlarmMessageService;
import com.expressway.vo.AlarmMessageVO;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 告警消息控制器
 * 处理告警消息相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/alarm/message")
public class AlarmMessageController {

    @Resource
    private AlarmMessageService alarmMessageService;

    /**
     * 分页查询告警消息列表
     *
     * @param queryParams 查询参数
     * @return 告警消息分页数据
     */
    @PostMapping("/list")
    public Result<PageInfo<AlarmMessageVO>> getAlarmMessageList(@Validated @RequestBody AlarmMessageQueryParamsDTO queryParams) {
        try {
            PageInfo<AlarmMessageVO> pageResult = alarmMessageService.getAlarmMessageList(queryParams);
            return Result.success(pageResult);
        } catch (RuntimeException e) {
            return Result.error("查询告警消息列表失败：" + e.getMessage());
        }
    }

    /**
     * 修改告警消息
     *
     * @param updateDTO 修改参数
     * @return 统一响应结果
     */
    @PostMapping("/update")
    public Result<?> updateAlarmMessage(@Validated @RequestBody AlarmMessageUpdateDTO updateDTO) {
        try {
            alarmMessageService.updateAlarmMessage(updateDTO);
            return Result.success("修改告警消息成功");
        } catch (AlarmMessageException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("修改告警消息失败：" + e.getMessage());
        }
    }


    /**
     * 确认告警
     */
    @PostMapping("/confirm")
    public Result<Void> confirmAlarmMessage(@Validated @RequestBody AlarmConfirmDTO alarmConfirm) {
        try {
            alarmMessageService.confirmAlarmMessage(alarmConfirm);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error("确认告警失败：" + e.getMessage());
        }
    }

    /**
     * 告警消息详情
     *
     * @param id 告警ID
     * @return 告警消息详情
     */
    @GetMapping("/{id}")
    public Result<AlarmMessageVO> getAlarmMessageById(@PathVariable("id") Long id) {
        try {
            AlarmMessageVO alarmMessage = alarmMessageService.getAlarmMessageById(id);
            return Result.success(alarmMessage);
        } catch (AlarmMessageException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询告警消息详情失败：" + e.getMessage());
        }
    }
}
