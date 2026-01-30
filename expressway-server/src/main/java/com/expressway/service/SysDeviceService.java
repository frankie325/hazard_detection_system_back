package com.expressway.service;

import com.expressway.dto.DeviceAddDTO;
import com.expressway.dto.DeviceUpdateDTO;
import com.expressway.entity.SysDevice;

import java.util.List;

public interface SysDeviceService {
    /**
     * 查询所有设备
     */
    List<SysDevice> getAllList();

    /**
     * 根据区域ID查询设备
     */
    List<SysDevice> getDeviceByAreaId(Long areaId);

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
}
