package com.expressway.handler;

import com.expressway.enumeration.DeviceType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 设备类型枚举类型处理器
 */
@MappedTypes(DeviceType.class)
public class DeviceTypeTypeHandler extends BaseTypeHandler<DeviceType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DeviceType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public DeviceType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return code != null ? getDeviceTypeByCode(code) : null;
    }

    @Override
    public DeviceType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return code != null ? getDeviceTypeByCode(code) : null;
    }

    @Override
    public DeviceType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return code != null ? getDeviceTypeByCode(code) : null;
    }

    /**
     * 根据代码获取设备类型枚举
     */
    private DeviceType getDeviceTypeByCode(String code) {
        if ("C".equals(code)) {
            return DeviceType.CAMERA;
        } else if ("S".equals(code)) {
            return DeviceType.SENSOR;
        }
        throw new IllegalArgumentException("未知的设备类型代码: " + code);
    }
}