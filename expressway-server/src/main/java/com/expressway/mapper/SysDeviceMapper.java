package com.expressway.mapper;

import com.expressway.entity.SysDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDeviceMapper {
    /**
     * 查询所有设备
     */
    List<SysDevice> selectAllDevice();

    /**
     * 根据区域ID查询设备
     */
    List<SysDevice> selectDeviceByAreaId(Long areaId);

    /**
     * 根据ID查询设备
     */
    SysDevice selectDeviceById(Long id);

    /**
     * 新增设备
     */
    int insertDevice(SysDevice sysDevice);

    /**
     * 更新设备
     */
    int updateDeviceById(SysDevice sysDevice);

    /**
     * 根据ID删除设备
     */
    int deleteDeviceById(Long id);

    /**
     * 批量删除设备
     */
    int batchDeleteDevice(@Param("ids") List<Long> ids);

    /**
     * 根据设备编码查询设备
     */
    SysDevice selectDeviceByCode(String deviceCode);
}
