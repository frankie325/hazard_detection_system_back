package com.expressway.service.impl;

import com.expressway.dto.DeviceAddDTO;
import com.expressway.dto.DeviceQueryParamsDTO;
import com.expressway.dto.DeviceUpdateDTO;
import com.expressway.entity.SysArea;
import com.expressway.entity.SysDevice;
import com.expressway.enumeration.DeviceStatus;
import com.expressway.exception.DeviceException;
import com.expressway.mapper.SysAreaMapper;
import com.expressway.mapper.SysDeviceMapper;
import com.expressway.service.SysDeviceService;
import com.expressway.vo.DeviceVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
