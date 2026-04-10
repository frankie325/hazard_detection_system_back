package com.expressway.vo.dashboard;

import lombok.Data;

import java.util.List;

/**
 * 直方图数据VO
 */
@Data
public class HistogramChartVO {
    /**
     * x轴分类数据
     */
    private List<String> xAxis;

    /**
     * y轴数据
     */
    private List<Long> data;
}
