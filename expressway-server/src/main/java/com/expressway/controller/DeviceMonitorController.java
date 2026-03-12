package com.expressway.controller;

import com.expressway.result.Result;
import com.expressway.service.SysDeviceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备监控控制器
 * 处理设备监控相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/detect/deviceMonitor")
public class DeviceMonitorController {

    @Resource
    private SysDeviceService sysDeviceService;

    /**
     * 获取设备树形结构
     */
    @GetMapping("/deviceTree")
    public Result<List<Map<String, Object>>> getDeviceTree() {
        try {
            List<Map<String, Object>> deviceTree = sysDeviceService.getDeviceTree();
            return Result.success(deviceTree);
        } catch (Exception e) {
            return Result.error("获取设备树形结构失败：" + e.getMessage());
        }
    }

    /**
     * 重启设备
     */
    @PostMapping("/restart/{deviceId}")
    public Result<?> restartDevice(@PathVariable("deviceId") Long deviceId) {
        try {
            sysDeviceService.restartDevice(deviceId);
            return Result.success("重启设备成功");
        } catch (Exception e) {
            return Result.error("重启设备失败：" + e.getMessage());
        }
    }

    /**
     * 打开检测预览
     */
    @PostMapping("/enablePreview/{deviceId}")
    public Result<?> enableDetectionPreview(@PathVariable("deviceId") Long deviceId) {
        try {
            sysDeviceService.enableDetectionPreview(deviceId);
            return Result.success("打开检测预览成功");
        } catch (Exception e) {
            return Result.error("打开检测预览失败：" + e.getMessage());
        }
    }

    /**
     * 关闭检测预览
     */
    @PostMapping("/disablePreview/{deviceId}")
    public Result<?> disableDetectionPreview(@PathVariable("deviceId") Long deviceId) {
        try {
            sysDeviceService.disableDetectionPreview(deviceId);
            return Result.success("关闭检测预览成功");
        } catch (Exception e) {
            return Result.error("关闭检测预览失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备实时状态
     */
    @GetMapping("/status/{deviceId}")
    public Result<Map<String, Object>> getDeviceRealTimeStatus(@PathVariable("deviceId") Long deviceId) {
        try {
            Map<String, Object> status = sysDeviceService.getDeviceRealTimeStatus(deviceId);
            return Result.success(status);
        } catch (Exception e) {
            return Result.error("获取设备实时状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取传感器实时数据
     */
    @GetMapping("/sensorData/{deviceId}")
    public Result<Map<String, Object>> getSensorRealTimeData(@PathVariable("deviceId") Long deviceId) {
        try {
            Map<String, Object> sensorData = sysDeviceService.getSensorRealTimeData(deviceId);
            return Result.success(sensorData);
        } catch (Exception e) {
            return Result.error("获取传感器实时数据失败：" + e.getMessage());
        }
    }
}
