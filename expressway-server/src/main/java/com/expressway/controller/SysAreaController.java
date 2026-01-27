package com.expressway.controller;


import com.expressway.dto.AreaAddDTO;
import com.expressway.dto.AreaTreeDTO;
import com.expressway.dto.AreaUpdateDTO;
import com.expressway.entity.SysArea;
import com.expressway.exception.AreaException;
import com.expressway.result.Result;
import com.expressway.service.SysAreaService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域管理控制器
 * 处理区域相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/user/sys/area")
public class SysAreaController {
    @Resource
    private SysAreaService sysAreaService;

    /**
     * 查询区域列表数据
     * @return 区域列表数据
     */
    @GetMapping("/list")
    public Result<List<SysArea>> getAllList(){
        try{
            List<SysArea> areaList = sysAreaService.getAllList();
            return Result.success(areaList);
        } catch (RuntimeException e) {
            return Result.error("查询区域列表数据：" + e.getMessage());
        }
    }

    /**
     * 查询区域树形结构
     * @return 区域树形结构数据
     */
    @GetMapping("/tree")
    public Result<List<AreaTreeDTO>> getAreaTree(){
        try{
            List<AreaTreeDTO> areaTree = sysAreaService.getAreaTree();
            return Result.success(areaTree);
        } catch (RuntimeException e) {
            return Result.error("查询区域树形结构：" + e.getMessage());
        }
    }

    /**
     * 新增区域
     * @param areaAddDTO 新增区域参数（已做参数校验）
     * @return 统一响应结果
     */
    @PostMapping("/add")
    public Result<?> addArea(@Validated @RequestBody AreaAddDTO areaAddDTO) {
        try {
            sysAreaService.addArea(areaAddDTO);
            return Result.success("新增区域成功");
        } catch (AreaException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("新增区域失败：" + e.getMessage());
        }
    }

    /**
     * 编辑区域
     * @param areaUpdateDTO 编辑区域参数（已做参数校验）
     * @return 统一响应结果
     */
    @PutMapping("/update")
    public Result<?> updateArea(@Validated @RequestBody AreaUpdateDTO areaUpdateDTO) {
        try {
            sysAreaService.updateArea(areaUpdateDTO);
            return Result.success("编辑区域成功");
        } catch (AreaException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("编辑区域失败：" + e.getMessage());
        }
    }

    /**
     * 单个删除区域
     * @param id 区域ID（路径参数）
     * @return 统一响应结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteAreaById(@PathVariable("id") Long id) {
        try {
            sysAreaService.deleteAreaById(id);
            return Result.success("删除区域成功");
        } catch (AreaException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除区域失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除区域
     * @param ids 区域ID列表（请求体）
     * @return 统一响应结果
     */
    @DeleteMapping("/batchDelete")
    public Result<?> batchDeleteArea(@RequestBody List<Long> ids) {
        try {
            sysAreaService.batchDeleteArea(ids);
            return Result.success("批量删除区域成功");
        } catch (AreaException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("批量删除区域失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询单个区域（用于编辑回显）
     * @param id 区域ID（路径参数）
     * @return 单个区域详情
     */
    @GetMapping("/{id}")
    public Result<SysArea> getAreaById(@PathVariable("id") Long id) {
        try {
            SysArea area = sysAreaService.getAreaById(id);
            return Result.success(area);
        } catch (AreaException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询区域详情失败：" + e.getMessage());
        }
    }
}
