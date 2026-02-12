package com.expressway.vo;

import com.expressway.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleVO extends SysRole {
    private String deptName;
}
