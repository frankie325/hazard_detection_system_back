package com.expressway.controller;

import com.expressway.constant.AuthConstant;
import com.expressway.context.BaseContext;
import com.expressway.dto.LoginDTO;
import com.expressway.result.Result;
import com.expressway.service.PermissionService;
import com.expressway.service.SysUserService;
import com.expressway.vo.LogoutResponseVO;
import com.expressway.vo.UserLoginVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登录登出认证接口
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private PermissionService permissionService;

    /**
     * 登录接口（POST请求，接收JSON参数）
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        log.info("员工登录：{}", loginDTO);
        UserLoginVO loginResponseVO = sysUserService.login(loginDTO);
        return Result.success(loginResponseVO);
    }

    /**
     * 登出接口（GET请求，从请求头获取令牌）
     */
    @GetMapping("/logout")
    public Result<LogoutResponseVO> logout(@RequestHeader(AuthConstant.TOKEN_HEADER) String token) {
        // 处理令牌前缀（前端传入格式：Bearer xxx）
        if (token.startsWith(AuthConstant.TOKEN_PREFIX)) {
            token = token.substring(AuthConstant.TOKEN_PREFIX.length());
        }
        LogoutResponseVO logoutResponseVO = sysUserService.logout(token);
        return Result.success(logoutResponseVO);
    }

    /**
     * 检查用户是否有指定权限
     */
    @GetMapping("/permission/check")
    public Result<Boolean> checkPermission(@RequestParam("permissionCode") String permissionCode) {
        try {
            Long userId = BaseContext.getCurrentId();
            boolean hasPermission = permissionService.hasPermission(userId, permissionCode);
            return Result.success(hasPermission);
        } catch (Exception e) {
            return Result.error("权限检查失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户所有权限
     */
    @GetMapping("/permissions")
    public Result<java.util.List<String>> getUserPermissions() {
        try {
            Long userId = BaseContext.getCurrentId();
            java.util.List<String> permissions = permissionService.getUserPermissions(userId);
            return Result.success(permissions);
        } catch (Exception e) {
            return Result.error("获取权限失败：" + e.getMessage());
        }
    }

    /**
     * 检查用户是否有指定角色
     */
    @GetMapping("/role/check")
    public Result<Boolean> checkRole(@RequestParam("roleCode") String roleCode) {
        try {
            Long userId = BaseContext.getCurrentId();
            boolean hasRole = permissionService.hasRole(userId, roleCode);
            return Result.success(hasRole);
        } catch (Exception e) {
            return Result.error("角色检查失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户所有角色
     */
    @GetMapping("/roles")
    public Result<java.util.List<String>> getUserRoles() {
        try {
            Long userId = BaseContext.getCurrentId();
            java.util.List<String> roles = permissionService.getUserRoles(userId);
            return Result.success(roles);
        } catch (Exception e) {
            return Result.error("获取角色失败：" + e.getMessage());
        }
    }
}