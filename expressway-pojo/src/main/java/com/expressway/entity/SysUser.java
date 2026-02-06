package com.expressway.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.Date;

/**
 * 用户实体（对应sys_user表）
 */
@Data
public class SysUser {
    private Long id; // 主键ID
    private String name; // 姓名
    private String gender; // 性别（M-男，F-女）
    private String username; // 登录用户名（唯一）

    @JsonIgnore
    private String password; // 加密后的密码
    private String phone; // 手机号
    private String idCard; // 身份证号
    private Long roleId; // 所属角色ID
    private Long deptId; // 所属部门ID
    private String remark; // 备注

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime; // 更新时间

    // 关联查询字段（非数据库字段，用于接收部门名称、角色名称）
    private String deptName;
    private String roleName;
}