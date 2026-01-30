package com.expressway.dto;

import lombok.Data;
import java.util.List;

@Data
public class AreaTreeDTO {
    private Long id;
    private Long parentId;
    private String areaName;
    private Long deptId;
    private Double length;
    private Integer laneCount;
    private String coordinates;
    private List<AreaTreeDTO> children;
}
