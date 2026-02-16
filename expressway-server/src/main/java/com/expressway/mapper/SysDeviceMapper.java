package com.expressway.mapper;

import com.expressway.dto.DeviceQueryParamsDTO;
import com.expressway.entity.SysDevice;
import com.expressway.vo.DeviceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDeviceMapper {
    /**
     * 查询所有设备
     */
    List<DeviceVO> selectAllDevice();

    /**
     * 根据条件分页查询设备
     */
    List<DeviceVO> selectDeviceList(DeviceQueryParamsDTO queryParams);

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
