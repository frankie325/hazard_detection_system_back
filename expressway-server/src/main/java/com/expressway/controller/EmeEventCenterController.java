package com.expressway.controller;

import com.expressway.dto.EmeEventAddDTO;
import com.expressway.dto.EmeEventStatusUpdateDTO;
import com.expressway.dto.EmeTimelineAddDTO;
import com.expressway.exception.EmeEventException;
import com.expressway.result.Result;
import com.expressway.service.EmeEventService;
import com.expressway.vo.EmeEventVO;
import com.expressway.vo.EmeTimelineVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件中心控制器
 * 处理事件中心相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/emergency/eventCenter")
public class EmeEventCenterController {

    @Resource
    private EmeEventService emeEventService;

    /**
     * 查询所有事件中心列表
     */
    @GetMapping("/allList")
    public Result<List<EmeEventVO>> getEmeEventList() {
        try {
            List<EmeEventVO> eventList = emeEventService.getEmeEventList();
            return Result.success(eventList);
        } catch (RuntimeException e) {
            return Result.error("查询事件列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询事件详情
     *
     * @param id 事件ID
     * @return 事件详情
     */
    @GetMapping("/detail/{id}")
    public Result<EmeEventVO> getEventDetail(@PathVariable Long id) {
        try {
            EmeEventVO eventVO = emeEventService.getEventById(id);
            return Result.success(eventVO);
        } catch (EmeEventException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询事件详情失败：" + e.getMessage());
        }
    }


    /**
     * 查询应急事件的时间线
     *
     * @param eventId 应急事件ID
     * @return 时间线列表
     */
    @GetMapping("/timeline/{eventId}")
    public Result<List<EmeTimelineVO>> getTimeline(@PathVariable Long eventId) {
        try {
            List<EmeTimelineVO> timelineList = emeEventService.getTimelineByEventId(eventId);
            return Result.success(timelineList);
        } catch (EmeEventException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询时间线失败：" + e.getMessage());
        }
    }

    /**
     * 新增事件
     *
     * @param addDTO 新增事件参数
     * @return 统一响应结果
     */
    @PostMapping("/add")
    public Result<?> addEmeEvent(@Validated @RequestBody EmeEventAddDTO addDTO) {
        try {
            emeEventService.createEmeEvent(addDTO);
            return Result.success("新增事件成功");
        } catch (EmeEventException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("新增事件失败：" + e.getMessage());
        }
    }

    /**
     * 更新事件状态
     *
     * @param updateDTO 状态更新参数
     * @return 统一响应结果
     */
    @PostMapping("/updateStatus")
    public Result<?> updateEventStatus(@Validated @RequestBody EmeEventStatusUpdateDTO updateDTO) {
        try {
            emeEventService.updateEventStatus(updateDTO);
            return Result.success("状态更新成功");
        } catch (EmeEventException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 生成时间线
     *
     * @param timeline 时间线参数
     * @return 统一响应结果
     */
    @PostMapping("/timeline")
    public Result<?> createTimeline(@Validated @RequestBody EmeTimelineAddDTO timeline) {
        try {
            emeEventService.createTimeline(timeline);
            return Result.success("生成时间线成功");
        } catch (EmeEventException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("生成时间线失败：" + e.getMessage());
        }
    }

    /**
     * 导出事件数据
     */
    @GetMapping("/export")
    public void exportEvents(HttpServletResponse response) {
        try {
            // 获取所有事件
            List<EmeEventVO> events = emeEventService.getEmeEventList();
            
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = "事件数据导出_" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xls";
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            
            // 写入Excel数据
            java.io.PrintWriter writer = response.getWriter();
            // 写入表头
            writer.println("事件ID\t事件名称\t事件类型\t事件等级\t事件状态\t创建时间\t更新时间\t位置\t责任部门\t关联告警ID");
            
            // 写入数据
            for (EmeEventVO event : events) {
                writer.println(event.getId() + "\t" +
                        (event.getEventName() != null ? event.getEventName() : "") + "\t" +
                        (event.getEventTypeName() != null ? event.getEventTypeName() : "") + "\t" +
                        (event.getEventLevelName() != null ? event.getEventLevelName() : "") + "\t" +
                        (event.getStatusName() != null ? event.getStatusName() : "") + "\t" +
                        (event.getCreateTime() != null ? event.getCreateTime() : "") + "\t" +
                        (event.getUpdateTime() != null ? event.getUpdateTime() : "") + "\t" +
                        (event.getLocation() != null ? event.getLocation() : "") + "\t" +
                        (event.getDeptName() != null ? event.getDeptName() : "") + "\t" +
                        (event.getAlarmId() != null ? event.getAlarmId() : ""));
            }
            
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
