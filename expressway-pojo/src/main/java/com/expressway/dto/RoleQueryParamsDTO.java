package com.expressway.dto;

import lombok.Data;

@Data
public class RoleQueryParamsDTO extends PageDTO {
    private String roleName;
    private String deptName;
}
