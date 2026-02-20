package com.expressway.controller;

import com.expressway.dto.DetectEventStreamQueryParamsDTO;
import com.expressway.result.Result;
import com.expressway.service.DetectEventStreamService;
import com.expressway.vo.DetectEventStreamVO;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 检测事件流
 */
@RestController
@RequestMapping("/detect/eventStream")
public class DetectEventStreamController {

    @Resource
    private DetectEventStreamService detectEventStreamService;

    /**
     * 获取检测事件流列表
     */
    @PostMapping("/list")
    public Result<PageInfo<DetectEventStreamVO>> getEventStreamList(@Validated @RequestBody DetectEventStreamQueryParamsDTO queryParams) {
        PageInfo<DetectEventStreamVO> pageInfo = detectEventStreamService.getEventStreamList(queryParams);
        return Result.success(pageInfo);
    }
}
