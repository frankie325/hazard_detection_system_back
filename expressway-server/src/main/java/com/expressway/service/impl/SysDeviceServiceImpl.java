package com.expressway.service.impl;

import com.expressway.dto.DeviceAddDTO;
import com.expressway.dto.DeviceQueryParamsDTO;
import com.expressway.dto.DeviceUpdateDTO;
import com.expressway.entity.SysArea;
import com.expressway.entity.SysDevice;
import com.expressway.enumeration.DeviceStatus;
import com.expressway.enumeration.DeviceType;
import com.expressway.exception.DeviceException;
import com.expressway.mapper.SysAreaMapper;
import com.expressway.mapper.SysDeviceMapper;
import com.expressway.service.SysDeviceService;
import com.expressway.vo.AreaVO;
import com.expressway.vo.DeviceVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SysDeviceServiceImpl implements SysDeviceService {
    @Resource
    private SysDeviceMapper sysDeviceMapper;

    @Resource
    private SysAreaMapper sysAreaMapper;

    /**
     * 分页查询设备列表
     */
    @Override
    public PageInfo<DeviceVO> getDeviceList(DeviceQueryParamsDTO queryParams) {
        PageHelper.startPage(queryParams.getCurrent(), queryParams.getSize());
        List<DeviceVO> deviceList = sysDeviceMapper.selectDeviceList(queryParams);
        return new PageInfo<>(deviceList);
    }

    /**
     * 新增设备
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDevice(DeviceAddDTO deviceAddDTO) {
        // 1. 校验所属区域是否存在
        SysArea area = sysAreaMapper.selectAreaById(deviceAddDTO.getAreaId());
        if (area == null) {
            throw new DeviceException("所属区域不存在");
        }

        // 2. 校验设备编码是否已存在
        SysDevice existDevice = sysDeviceMapper.selectDeviceByCode(deviceAddDTO.getDeviceCode());
        if (existDevice != null) {
            throw new DeviceException("设备编码已存在");
        }

        // 3. DTO转实体
        SysDevice sysDevice = new SysDevice();
        BeanUtils.copyProperties(deviceAddDTO, sysDevice);
        sysDevice.setStatus(DeviceStatus.ONLINE);

        // 4. 插入数据库
        int insertResult = sysDeviceMapper.insertDevice(sysDevice);
        if (insertResult != 1) {
            throw new DeviceException("新增设备失败");
        }
    }

    /**
     * 编辑设备
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDevice(DeviceUpdateDTO deviceUpdateDTO) {
        // 1. 校验设备是否存在
        SysDevice existDevice = sysDeviceMapper.selectDeviceById(deviceUpdateDTO.getId());
        if (existDevice == null) {
            throw new DeviceException("设备不存在");
        }

        // 2. 校验所属区域是否存在
        SysArea area = sysAreaMapper.selectAreaById(deviceUpdateDTO.getAreaId());
        if (area == null) {
            throw new DeviceException("所属区域不存在");
        }

        // 3. 校验设备编码是否被其他设备使用
        SysDevice codeDevice = sysDeviceMapper.selectDeviceByCode(deviceUpdateDTO.getDeviceCode());
        if (codeDevice != null && !codeDevice.getId().equals(deviceUpdateDTO.getId())) {
            throw new DeviceException("设备编码已被其他设备使用");
        }

        // 4. DTO转实体
        SysDevice sysDevice = new SysDevice();
        BeanUtils.copyProperties(deviceUpdateDTO, sysDevice);
        sysDevice.setStatus(existDevice.getStatus());

        // 5. 更新数据库
        int updateResult = sysDeviceMapper.updateDeviceById(sysDevice);
        if (updateResult != 1) {
            throw new DeviceException("编辑设备失败");
        }
    }

    /**
     * 根据ID删除设备
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeviceById(Long id) {
        // 1. 校验设备是否存在
        SysDevice existDevice = sysDeviceMapper.selectDeviceById(id);
        if (existDevice == null) {
            throw new DeviceException("设备不存在");
        }

        // 2. 执行删除
        int deleteResult = sysDeviceMapper.deleteDeviceById(id);
        if (deleteResult != 1) {
            throw new DeviceException("删除设备失败");
        }
    }

    /**
     * 批量删除设备
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteDevice(List<Long> ids) {
        // 1. 校验选择的数据
        if (ids == null || ids.isEmpty()) {
            throw new DeviceException("请选择需要删除的数据！");
        }

        // 2. 逐个检查设备是否存在
        for (Long id : ids) {
            SysDevice device = sysDeviceMapper.selectDeviceById(id);
            if (device == null) {
                throw new DeviceException("设备ID：" + id + " 不存在，批量删除失败");
            }
        }

        // 3. 批量执行删除
        int deleteResult = sysDeviceMapper.batchDeleteDevice(ids);
        if (deleteResult == 0) {
            throw new DeviceException("批量删除设备失败");
        }
    }

    /**
     * 根据ID查询设备
     */
    @Override
    public SysDevice getDeviceById(Long id) {
        if (id == null) {
            throw new DeviceException("设备ID不能为空");
        }
        return sysDeviceMapper.selectDeviceById(id);
    }

    /**
     * 重启设备
     */
    @Override
    public void restartDevice(Long deviceId) {
        // 1. 校验设备是否存在
        SysDevice device = sysDeviceMapper.selectDeviceById(deviceId);
        if (device == null) {
            throw new DeviceException("设备不存在");
        }
        
        // 2. 模拟设备重启操作
        // 实际项目中这里会调用设备管理接口或发送重启命令
        System.out.println("重启设备：" + device.getDeviceName() + " (" + device.getDeviceCode() + ")");
        
        // 3. 可以在这里添加重启日志记录
    }

    /**
     * 打开检测预览
     */
    @Override
    public void enableDetectionPreview(Long deviceId) {
        // 1. 校验设备是否存在
        SysDevice device = sysDeviceMapper.selectDeviceById(deviceId);
        if (device == null) {
            throw new DeviceException("设备不存在");
        }
        
        // 2. 模拟打开检测预览操作
        // 实际项目中这里会调用视频流接口或启动检测服务
        System.out.println("打开检测预览：" + device.getDeviceName() + " (" + device.getDeviceCode() + ")");
        
        // 3. 可以在这里添加操作日志记录
    }

    /**
     * 关闭检测预览
     */
    @Override
    public void disableDetectionPreview(Long deviceId) {
        // 1. 校验设备是否存在
        SysDevice device = sysDeviceMapper.selectDeviceById(deviceId);
        if (device == null) {
            throw new DeviceException("设备不存在");
        }
        
        // 2. 模拟关闭检测预览操作
        // 实际项目中这里会调用视频流接口或停止检测服务
        System.out.println("关闭检测预览：" + device.getDeviceName() + " (" + device.getDeviceCode() + ")");
        
        // 3. 可以在这里添加操作日志记录
    }

    /**
     * 获取设备实时状态
     */
    @Override
    public Map<String, Object> getDeviceRealTimeStatus(Long deviceId) {
        // 1. 校验设备是否存在
        SysDevice device = sysDeviceMapper.selectDeviceById(deviceId);
        if (device == null) {
            throw new DeviceException("设备不存在");
        }
        
        // 2. 构建设备实时状态
        Map<String, Object> statusMap = new java.util.HashMap<>();
        statusMap.put("deviceId", device.getId());
        statusMap.put("deviceName", device.getDeviceName());
        statusMap.put("deviceCode", device.getDeviceCode());
        statusMap.put("status", device.getStatus().name());
        statusMap.put("deviceType", device.getDeviceType() != null ? device.getDeviceType().getDescription() : null);
        statusMap.put("installLocation", device.getLocation());
        statusMap.put("ipAddress", device.getIpAddress());
        statusMap.put("model", device.getModel());
        statusMap.put("detectionStatus", "检测已开启"); // 模拟检测状态
        statusMap.put("lastOnlineTime", new java.util.Date());
        
        return statusMap;
    }

    /**
     * 获取传感器实时数据
     */
    @Override
    public Map<String, Object> getSensorRealTimeData(Long deviceId) {
        // 1. 校验设备是否存在
        SysDevice device = sysDeviceMapper.selectDeviceById(deviceId);
        if (device == null) {
            throw new DeviceException("设备不存在");
        }
        
        // 2. 构建传感器实时数据
        Map<String, Object> sensorData = new java.util.HashMap<>();
        sensorData.put("deviceId", device.getId());
        sensorData.put("deviceName", device.getDeviceName());
        
        // 模拟传感器数据
        if (device.getDeviceType() != null && device.getDeviceType() == DeviceType.SENSOR) { // 传感器设备
            // 温度数据（20-30℃）
            double temperature = 20 + Math.random() * 10;
            sensorData.put("temperature", String.format("%.1f °C", temperature));
            
            // 振动数据（0.5-4.5 mm/s）
            double vibration = 0.5 + Math.random() * 4;
            sensorData.put("vibration", String.format("%.2f mm/s", vibration));
            
            // CO浓度数据（5-35 ppm）
            int coConcentration = 5 + (int)(Math.random() * 30);
            sensorData.put("coConcentration", coConcentration + " ppm");
            
            sensorData.put("timestamp", new java.util.Date());
        } else {
            sensorData.put("error", "该设备不是传感器类型");
        }
        
        return sensorData;
    }

    /**
     * 获取设备树形结构
     */
    @Override
    public List<Map<String, Object>> getDeviceTree() {
        // 1. 获取所有区域
        List<AreaVO> areas = sysAreaMapper.selectAllArea();
        
        // 2. 获取所有设备
        List<SysDevice> devices = sysDeviceMapper.selectAllDevices();
        
        // 3. 构建树形结构
        List<Map<String, Object>> treeNodes = new java.util.ArrayList<>();
        
        for (AreaVO area : areas) {
            Map<String, Object> areaNode = new java.util.HashMap<>();
            areaNode.put("id", "area_" + area.getId());
            areaNode.put("name", area.getAreaName());
            areaNode.put("type", "area");
            areaNode.put("deviceCount", 0);
            
            List<Map<String, Object>> deviceNodes = new java.util.ArrayList<>();
            int count = 0;
            
            for (SysDevice device : devices) {
                if (device.getAreaId().equals(area.getId())) {
                    Map<String, Object> deviceNode = new java.util.HashMap<>();
                    deviceNode.put("id", device.getId().toString());
                    deviceNode.put("name", device.getDeviceName());
                    deviceNode.put("type", device.getDeviceType());
                    deviceNode.put("deviceCode", device.getDeviceCode());
                    deviceNode.put("location", device.getLocation());
                    deviceNode.put("status", device.getStatus().name());
                    deviceNodes.add(deviceNode);
                    count++;
                }
            }
            
            areaNode.put("deviceCount", count);
            areaNode.put("children", deviceNodes);
            treeNodes.add(areaNode);
        }
        
        return treeNodes;
    }
}
