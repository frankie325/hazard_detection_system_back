package com.expressway.handler;

import com.expressway.enumeration.EmeEventStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 应急事件状态枚举类型处理器
 * 将数据库中的中文描述映射到枚举常量
 */
@MappedTypes(EmeEventStatus.class)
public class EmeEventStatusTypeHandler extends BaseTypeHandler<EmeEventStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EmeEventStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDescription());
    }

    @Override
    public EmeEventStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return convertToEnum(value);
    }

    @Override
    public EmeEventStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return convertToEnum(value);
    }

    @Override
    public EmeEventStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return convertToEnum(value);
    }

    /**
     * 将数据库中的中文描述转换为枚举常量
     */
    private EmeEventStatus convertToEnum(String value) {
        if (value == null) {
            return null;
        }
        for (EmeEventStatus status : EmeEventStatus.values()) {
            if (status.getDescription().equals(value) || status.getCode().equals(value) || status.name().equals(value)) {
                return status;
            }
        }
        // 如果找不到匹配的枚举，返回默认值或抛出异常
        throw new IllegalArgumentException("未知的应急事件状态: " + value);
    }
}