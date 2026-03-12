package com.expressway.handler;

import com.expressway.enumeration.DeviceStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 设备状态枚举类型处理器
 */
@MappedTypes(DeviceStatus.class)
public class DeviceStatusTypeHandler extends BaseTypeHandler<DeviceStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DeviceStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, getCodeByDeviceStatus(parameter));
    }

    @Override
    public DeviceStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return code != null ? getDeviceStatusByCode(code) : null;
    }

    @Override
    public DeviceStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return code != null ? getDeviceStatusByCode(code) : null;
    }

    @Override
    public DeviceStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return code != null ? getDeviceStatusByCode(code) : null;
    }

    /**
     * 根据代码获取设备状态枚举
     */
    private DeviceStatus getDeviceStatusByCode(String code) {
        if ("O".equals(code)) {
            return DeviceStatus.ONLINE;
        } else if ("F".equals(code)) {
            return DeviceStatus.OFFLINE;
        } else if ("M".equals(code)) {
            return DeviceStatus.MAINTENANCE;
        }
        throw new IllegalArgumentException("未知的设备状态代码: " + code);
    }

    /**
     * 根据设备状态枚举获取代码
     */
    private String getCodeByDeviceStatus(DeviceStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case ONLINE:
                return "O";
            case OFFLINE:
                return "F";
            case MAINTENANCE:
                return "M";
            default:
                throw new IllegalArgumentException("未知的设备状态: " + status);
        }
    }
}