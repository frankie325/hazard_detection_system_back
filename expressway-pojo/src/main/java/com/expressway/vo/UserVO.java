package com.expressway.vo;

import com.expressway.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户视图对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserVO extends SysUser {
    private String roleName;   // 角色名称
    private String deptName;   // 角色名称
}
