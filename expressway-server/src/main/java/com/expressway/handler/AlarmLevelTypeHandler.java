package com.expressway.handler;

import com.expressway.enumeration.AlarmLevel;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 告警等级枚举类型处理器
 */
@MappedTypes(AlarmLevel.class)
public class AlarmLevelTypeHandler extends BaseTypeHandler<AlarmLevel> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AlarmLevel parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public AlarmLevel getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null;
        }
        for (AlarmLevel level : AlarmLevel.values()) {
            if (level.getCode().equals(value)) {
                return level;
            }
        }
        return null;
    }

    @Override
    public AlarmLevel getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null) {
            return null;
        }
        for (AlarmLevel level : AlarmLevel.values()) {
            if (level.getCode().equals(value)) {
                return level;
            }
        }
        return null;
    }

    @Override
    public AlarmLevel getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null) {
            return null;
        }
        for (AlarmLevel level : AlarmLevel.values()) {
            if (level.getCode().equals(value)) {
                return level;
            }
        }
        return null;
    }
}
