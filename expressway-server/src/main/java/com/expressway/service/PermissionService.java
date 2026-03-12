package com.expressway.service;

import java.util.List;

/**
 * 权限服务
 * 用于检查用户权限
 */
public interface PermissionService {
    /**
     * 检查用户是否有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return 是否有权限
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 获取用户所有权限编码
     * @param userId 用户ID
     * @return 权限编码列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 检查用户是否有角色
     * @param userId 用户ID
     * @param roleCode 角色编码
     * @return 是否有角色
     */
    boolean hasRole(Long userId, String roleCode);

    /**
     * 获取用户所有角色编码
     * @param userId 用户ID
     * @return 角色编码列表
     */
    List<String> getUserRoles(Long userId);
}
