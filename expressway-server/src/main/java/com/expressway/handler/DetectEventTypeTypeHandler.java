package com.expressway.handler;

import com.expressway.enumeration.DetectEventType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 检测事件类型枚举类型处理器
 */
@MappedTypes(DetectEventType.class)
public class DetectEventTypeTypeHandler extends BaseTypeHandler<DetectEventType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DetectEventType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public DetectEventType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return getByValue(value);
    }

    @Override
    public DetectEventType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return getByValue(value);
    }

    @Override
    public DetectEventType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return getByValue(value);
    }

    /**
     * 根据代码或描述获取枚举值
     */
    private DetectEventType getByValue(String value) {
        if (value == null) {
            return null;
        }
        for (DetectEventType type : DetectEventType.values()) {
            if (type.getCode().equals(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
