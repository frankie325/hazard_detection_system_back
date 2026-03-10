package com.expressway.handler;

import com.expressway.enumeration.ActionType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ActionType.class)
public class ActionTypeTypeHandler extends BaseTypeHandler<ActionType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ActionType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDescription());
    }

    @Override
    public ActionType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return getByValue(value);
    }

    @Override
    public ActionType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return getByValue(value);
    }

    @Override
    public ActionType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return getByValue(value);
    }

    private ActionType getByValue(String value) {
        if (value == null) {
            return null;
        }
        for (ActionType type : ActionType.values()) {
            if (type.getCode().equals(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        return null;
    }
}