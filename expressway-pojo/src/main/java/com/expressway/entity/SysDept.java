package com.expressway.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysDept {
    private Long id;                // 部门ID
    private Long parentId;          // 上级部门ID（0为根节点）
    private String deptName;        // 部门名称（必填）
    private String deptCode;        // 部门编码（非必填）
    private String description;     // 描述（非必填）
    private Date createTime;        // 创建时间（后台自动生成）
    private Date updateTime;        // 更新时间（后台自动生成）

    // 树形结构专用字段（非数据库字段）
    private List<SysDept> children; // 子部门列表
}
