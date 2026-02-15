package com.expressway.dto;

import lombok.Data;

@Data
public class AreaQueryParamsDTO extends PageDTO {
    private String areaName;
    private String deptName;
}
