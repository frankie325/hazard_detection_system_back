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
}
