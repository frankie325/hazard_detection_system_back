package com.expressway.controller;

import com.expressway.dto.DeviceAddDTO;
import com.expressway.dto.DeviceQueryParamsDTO;
import com.expressway.dto.DeviceUpdateDTO;
import com.expressway.entity.SysDevice;
import com.expressway.exception.DeviceException;
import com.expressway.result.Result;
import com.expressway.service.SysDeviceService;
import com.expressway.vo.DeviceVO;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理控制器
 * 处理设备相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/sys/device")
public class SysDeviceController {
    @Resource
    private SysDeviceService sysDeviceService;

    /**
     * 分页查询设备列表
     *
     * @param queryParams 查询参数（可选）
     * @return 设备分页数据
     */
    @PostMapping("/list")
    public Result<PageInfo<DeviceVO>> getDeviceList(@Validated @RequestBody DeviceQueryParamsDTO queryParams) {
        try {
            PageInfo<DeviceVO> pageResult = sysDeviceService.getDeviceList(queryParams);
            return Result.success(pageResult);
        } catch (RuntimeException e) {
            return Result.error("查询设备列表失败：" + e.getMessage());
        }
    }

    /**
     * 新增设备
     *
     * @param deviceAddDTO 新增设备参数（已做参数校验）
     * @return 统一响应结果
     */
    @PostMapping("/add")
    public Result<?> addDevice(@Validated @RequestBody DeviceAddDTO deviceAddDTO) {
        try {
            sysDeviceService.addDevice(deviceAddDTO);
            return Result.success("新增设备成功");
        } catch (DeviceException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("新增设备失败：" + e.getMessage());
        }
    }

    /**
     * 编辑设备
     *
     * @param deviceUpdateDTO 编辑设备参数（已做参数校验）
     * @return 统一响应结果
     */
    @PutMapping("/update")
    public Result<?> updateDevice(@Validated @RequestBody DeviceUpdateDTO deviceUpdateDTO) {
        try {
            sysDeviceService.updateDevice(deviceUpdateDTO);
            return Result.success("编辑设备成功");
        } catch (DeviceException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("编辑设备失败：" + e.getMessage());
        }
    }

    /**
     * 单个删除设备
     *
     * @param id 设备ID（路径参数）
     * @return 统一响应结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteDeviceById(@PathVariable("id") Long id) {
        try {
            sysDeviceService.deleteDeviceById(id);
            return Result.success("删除设备成功");
        } catch (DeviceException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除设备失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除设备
     *
     * @param ids 设备ID列表（请求体）
     * @return 统一响应结果
     */
    @DeleteMapping("/batchDelete")
    public Result<?> batchDeleteDevice(@RequestBody List<Long> ids) {
        try {
            sysDeviceService.batchDeleteDevice(ids);
            return Result.success("批量删除设备成功");
        } catch (DeviceException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("批量删除设备失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询单个设备（用于编辑回显）
     *
     * @param id 设备ID（路径参数）
     * @return 单个设备详情
     */
    @GetMapping("/{id}")
    public Result<SysDevice> getDeviceById(@PathVariable("id") Long id) {
        try {
            SysDevice device = sysDeviceService.getDeviceById(id);
            return Result.success(device);
        } catch (DeviceException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询设备详情失败：" + e.getMessage());
        }
    }
}
