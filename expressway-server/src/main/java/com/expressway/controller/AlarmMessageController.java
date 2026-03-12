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
import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * 导出告警消息
     */
    @PostMapping("/export")
    public void exportAlarmMessages(@Validated @RequestBody AlarmMessageQueryParamsDTO queryParams, HttpServletResponse response) {
        try {
            // 获取所有告警消息
            java.util.List<AlarmMessageVO> alarmMessages = alarmMessageService.getAllAlarmMessages(queryParams);
            
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = "告警消息导出_" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xls";
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            
            // 写入Excel数据
            java.io.PrintWriter writer = response.getWriter();
            // 写入表头
            writer.println("告警ID\t告警名称\t告警类型\t告警等级\t告警状态\t创建时间\t确认人ID\t关闭时间\t关闭原因\t处理结果\t位置\t设备名称");
            
            // 写入数据
            for (AlarmMessageVO message : alarmMessages) {
                writer.println(message.getId() + "\t" +
                        (message.getAlarmName() != null ? message.getAlarmName() : "") + "\t" +
                        (message.getEventTypeName() != null ? message.getEventTypeName() : "") + "\t" +
                        (message.getAlarmLevelName() != null ? message.getAlarmLevelName() : "") + "\t" +
                        (message.getAlarmStatusName() != null ? message.getAlarmStatusName() : "") + "\t" +
                        (message.getCreateTime() != null ? message.getCreateTime() : "") + "\t" +
                        (message.getConfirmedBy() != null ? message.getConfirmedBy() : "") + "\t" +
                        (message.getCloseTime() != null ? message.getCloseTime() : "") + "\t" +
                        (message.getCloseReason() != null ? message.getCloseReason() : "") + "\t" +
                        (message.getProcessingResult() != null ? message.getProcessingResult() : "") + "\t" +
                        (message.getLocation() != null ? message.getLocation() : "") + "\t" +
                        (message.getDeviceName() != null ? message.getDeviceName() : ""));
            }
            
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
