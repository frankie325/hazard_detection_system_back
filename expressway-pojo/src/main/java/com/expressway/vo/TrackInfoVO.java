package com.expressway.vo;

import com.expressway.enumeration.DetectEventType;
import lombok.Data;

import java.util.List;

/**
 * 跟踪目标信息VO
 */
@Data
public class TrackInfoVO {
    private DetectEventType type;
    /**
     * 跟踪ID
     */
    private Integer trackId;

    /**
     * 类别名称
     */
    private String className;

    /**
     * 类别ID
     */
    private Integer classId;

    /**
     * 置信度
     */
    private Double confidence;

    /**
     * 边界框 [x1, y1, x2, y2]
     */
    private List<Double> bbox;

    /**
     * 持续帧数
     */
    private Integer durationFrames;

    /**
     * 持续时长（秒）
     */
    private Double durationSeconds;
}
