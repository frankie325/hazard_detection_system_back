package com.expressway.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 部门树型响应VO
 */
@Data
public class DeptTreeVO {
    private Long id;                // 部门ID
    private Long parentId;          // 上级部门ID
    private String deptName;        // 部门名称
    private String deptCode;        // 部门编码
    private String description;     // 描述
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;      // 创建时间
    private List<DeptTreeVO> children; // 子部门列表
}