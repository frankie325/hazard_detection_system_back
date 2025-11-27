package com.expressway.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录成功后返回的用户信息VO（脱敏展示）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO {
    private Long id; // 用户ID
    private String name; // 姓名
    private String username; // 用户名
    private String gender; // 性别
    private String phone; // 手机号
    private String idCard; // 身份证号（前端自行脱敏）
    private Long deptId; // 部门ID
    private String deptName; // 部门名称
    private Long roleId; // 角色ID
    private String roleName; // 角色名称
    private String token; // 角色令牌
}