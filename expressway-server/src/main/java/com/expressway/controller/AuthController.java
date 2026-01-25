package com.expressway.controller;

import com.expressway.constant.AuthConstant;
import com.expressway.dto.LoginDTO;
import com.expressway.result.Result;
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
@RequestMapping("/user/auth")
@Slf4j
public class AuthController {

    @Resource
    private SysUserService sysUserService;

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
}