package com.expressway.vo.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 饼图数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieChartDataVO {
    /**
     * 数值
     */
    private Long value;

    /**
     * 名称
     */
    private String name;
}
