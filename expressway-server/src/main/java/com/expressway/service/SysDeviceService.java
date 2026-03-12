package com.expressway.service;

import com.expressway.dto.DeviceAddDTO;
import com.expressway.dto.DeviceQueryParamsDTO;
import com.expressway.dto.DeviceUpdateDTO;
import com.expressway.entity.SysDevice;
import com.expressway.vo.DeviceVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface SysDeviceService {
    /**
     * 分页查询设备列表
     */
    PageInfo<DeviceVO> getDeviceList(DeviceQueryParamsDTO queryParams);

    /**
     * 新增设备
     */
    void addDevice(DeviceAddDTO deviceAddDTO);

    /**
     * 编辑设备
     */
    void updateDevice(DeviceUpdateDTO deviceUpdateDTO);

    /**
     * 根据ID删除设备
     */
    void deleteDeviceById(Long id);

    /**
     * 批量删除设备
     */
    void batchDeleteDevice(List<Long> ids);

    /**
     * 根据ID查询设备
     */
    SysDevice getDeviceById(Long id);

    /**
     * 重启设备
     */
    void restartDevice(Long deviceId);

    /**
     * 打开检测预览
     */
    void enableDetectionPreview(Long deviceId);

    /**
     * 关闭检测预览
     */
    void disableDetectionPreview(Long deviceId);

    /**
     * 获取设备实时状态
     */
    Map<String, Object> getDeviceRealTimeStatus(Long deviceId);

    /**
     * 获取传感器实时数据
     */
    Map<String, Object> getSensorRealTimeData(Long deviceId);

    /**
     * 获取设备树形结构
     */
    List<Map<String, Object>> getDeviceTree();
}
