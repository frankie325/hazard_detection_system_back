package com.expressway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增区域入参DTO
 */
@Data
public class AreaAddDTO {
    @NotBlank(message = "区域名称不能为空")
    @Size(max = 32, message = "区域名称长度不能超过32个字符")
    private String areaName;      // 区域名称（必填）

    @NotNull(message = "负责部门不能为空")
    private Long deptId;          // 负责部门ID（必填）

    private Double length;        // 区域长度（非必填）

    private Integer laneCount;    // 车道数（非必填）

    private String coordinates;   // 坐标信息（非必填）
}
