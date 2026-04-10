package com.expressway.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * MySQL POINT类型与List<Double>的转换处理器
 * 数据库: POINT(116.404 39.915)
 * Java: [116.404, 39.915]  [经度, 纬度]
 */
@MappedTypes(List.class)
public class PointTypeHandler extends BaseTypeHandler<List<Double>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Double> parameter, JdbcType jdbcType) throws SQLException {
        // [116.404, 39.915] -> "POINT(116.404 39.915)"
        if (parameter != null && parameter.size() >= 2) {
            String pointWkt = String.format("POINT(%s %s)", parameter.get(0), parameter.get(1));
            ps.setString(i, pointWkt);
        } else {
            ps.setNull(i, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public List<Double> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parsePoint(rs.getString(columnName));
    }

    @Override
    public List<Double> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parsePoint(rs.getString(columnIndex));
    }

    @Override
    public List<Double> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parsePoint(cs.getString(columnIndex));
    }

    /**
     * 解析POINT字符串为坐标列表
     * "POINT(116.404 39.915)" -> [116.404, 39.915]
     */
    private List<Double> parsePoint(String pointStr) {
        if (pointStr == null || pointStr.isEmpty()) {
            return null;
        }
        
        // 解析格式: POINT(116.404 39.915)
        if (pointStr.startsWith("POINT(") && pointStr.endsWith(")")) {
            String coords = pointStr.substring(6, pointStr.length() - 1); // 去掉 "POINT(" 和 ")"
            String[] parts = coords.split("\\s+");
            if (parts.length >= 2) {
                try {
                    Double lon = Double.parseDouble(parts[0]);
                    Double lat = Double.parseDouble(parts[1]);
                    return Arrays.asList(lon, lat);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
