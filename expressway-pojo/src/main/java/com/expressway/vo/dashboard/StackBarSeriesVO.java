package com.expressway.vo.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 堆叠柱状图系列数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StackBarSeriesVO {
    /**
     * 系列名称（如：低级、中级、高级、紧急）
     */
    private String name;

    /**
     * 数据数组，每个元素对应x轴的一个分类
     */
    private List<Long> data;
}
