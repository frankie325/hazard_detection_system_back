package com.expressway.vo.dashboard;

import lombok.Data;

import java.util.List;

/**
 * 堆叠柱状图数据VO
 */
@Data
public class StackBarChartVO {
    /**
     * x轴分类数据
     */
    private List<String> xAxis;

    /**
     * 系列数据列表
     */
    private List<StackBarSeriesVO> series;
}
