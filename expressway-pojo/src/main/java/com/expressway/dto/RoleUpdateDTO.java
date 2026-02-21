package com.expressway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 编辑角色DTO
 */
@Data
public class RoleUpdateDTO {
    @NotNull(message = "角色ID不能为空")
    private Long id;                  // 角色ID
    private String roleName;             // 角色名称（可选）
    private Long deptId;                 // 所属部门ID（可选）
    private String remark;                // 备注（可选）
}
