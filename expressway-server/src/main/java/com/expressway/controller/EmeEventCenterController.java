package com.expressway.controller;

import com.expressway.dto.EmeEventAddDTO;
import com.expressway.exception.EmeEventException;
import com.expressway.result.Result;
import com.expressway.service.EmeEventService;
import com.expressway.vo.EmeEventVO;
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
}
