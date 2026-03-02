package com.expressway.controller;

import com.expressway.dto.EmeEventResourceBindDTO;
import com.expressway.result.Result;
import com.expressway.service.EmeResourceService;
import com.expressway.vo.EmeResourceVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件中心资源控制器
 * 处理事件中心相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/emergency/resource")
public class EmeEventResourceController {

    @Resource
    private EmeResourceService emeResourceService;

    /**
     * 查询所有资源池列表
     */
    @GetMapping("/allList")
    public Result<List<EmeResourceVO>> getEmeResourceList() {
        try {
            List<EmeResourceVO> resourceList = emeResourceService.getEmeResourceList();
            return Result.success(resourceList);
        } catch (RuntimeException e) {
            return Result.error("查询资源池列表失败：" + e.getMessage());
        }
    }

    /**
     * 应急事件和资源绑定
     */
    @PostMapping("/bind")
    public Result<?> bindEmeEventResource(@Validated @RequestBody EmeEventResourceBindDTO bindDTO) {
        try {
            emeResourceService.bindEventResource(bindDTO);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error("绑定失败：" + e.getMessage());
        }
    }
}
