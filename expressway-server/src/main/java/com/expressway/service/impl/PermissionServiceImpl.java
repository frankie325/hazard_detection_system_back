package com.expressway.service.impl;

import com.expressway.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限服务实现
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        // 模拟权限检查逻辑
        // 实际项目中应该从数据库查询用户权限
        List<String> userPermissions = getUserPermissions(userId);
        return userPermissions.contains(permissionCode);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        // 模拟用户权限数据
        // 实际项目中应该从数据库查询用户权限
        List<String> permissions = new ArrayList<>();
        
        // 管理员用户（ID=1）拥有所有权限
        if (userId == 1) {
            permissions.add("sys:user:view");
            permissions.add("sys:user:add");
            permissions.add("sys:user:edit");
            permissions.add("sys:user:delete");
            permissions.add("sys:role:view");
            permissions.add("sys:role:add");
            permissions.add("sys:role:edit");
            permissions.add("sys:role:delete");
            permissions.add("sys:dept:view");
            permissions.add("sys:dept:add");
            permissions.add("sys:dept:edit");
            permissions.add("sys:dept:delete");
            permissions.add("sys:area:view");
            permissions.add("sys:area:add");
            permissions.add("sys:area:edit");
            permissions.add("sys:area:delete");
            permissions.add("sys:device:view");
            permissions.add("sys:device:add");
            permissions.add("sys:device:edit");
            permissions.add("sys:device:delete");
            permissions.add("sys:device:control");
            permissions.add("alarm:view");
            permissions.add("alarm:confirm");
            permissions.add("alarm:close");
            permissions.add("event:view");
            permissions.add("event:add");
            permissions.add("event:edit");
            permissions.add("event:close");
            permissions.add("system:dashboard");
            permissions.add("system:health");
        } else {
            // 普通用户拥有基础权限
            permissions.add("sys:device:view");
            permissions.add("alarm:view");
            permissions.add("event:view");
            permissions.add("system:dashboard");
        }
        
        return permissions;
    }

    @Override
    public boolean hasRole(Long userId, String roleCode) {
        // 模拟角色检查逻辑
        // 实际项目中应该从数据库查询用户角色
        List<String> userRoles = getUserRoles(userId);
        return userRoles.contains(roleCode);
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        // 模拟用户角色数据
        // 实际项目中应该从数据库查询用户角色
        List<String> roles = new ArrayList<>();
        
        // 管理员用户（ID=1）拥有管理员角色
        if (userId == 1) {
            roles.add("admin");
            roles.add("operator");
        } else {
            // 普通用户拥有操作员角色
            roles.add("operator");
        }
        
        return roles;
    }
}
