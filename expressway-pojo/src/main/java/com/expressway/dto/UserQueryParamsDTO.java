package com.expressway.dto;

import lombok.Data;

/**
 * 用户查询参数DTO
 */
@Data
public class UserQueryParamsDTO extends PageDTO {
    private String name;      // 姓名
    private String username;  // 用户名
    private String roleName;  // 角色名称
    private String deptName;  // 所属部门
}
