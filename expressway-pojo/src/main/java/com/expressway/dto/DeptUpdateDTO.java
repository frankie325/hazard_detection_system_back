package com.expressway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 编辑部门入参DTO
 */
@Data
public class DeptUpdateDTO {
    @NotNull(message = "部门ID不能为空")
    private Long id;                // 部门ID（必填）

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 32, message = "部门名称长度不能超过32个字符")
    private String deptName;        // 部门名称（必填）

    private Long parentId = 0L;     // 上级部门ID（默认根节点0）

    private String deptCode;        // 部门编码（非必填）

    private String description;     // 描述（非必填）
}
