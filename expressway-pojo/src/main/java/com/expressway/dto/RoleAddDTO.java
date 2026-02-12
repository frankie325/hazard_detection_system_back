package com.expressway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleAddDTO {
    @NotBlank(message = "角色名称不能为空")
    private String roleName; // 角色名称
    @NotNull(message = "所属部门不能为空")
    private Long deptId; // 所属部门
    private String remark; // 备注
    private String permissions = ""; // 权限字段
}
