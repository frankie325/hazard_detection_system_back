package com.expressway.service;


import com.expressway.dto.LoginDTO;
import com.expressway.vo.LogoutResponseVO;
import com.expressway.vo.UserLoginVO;

/**
 * 用户认证相关业务接口
 */
public interface SysUserService {
    /**
     * 登录业务（验证用户名密码，生成令牌）
     */
    UserLoginVO login(LoginDTO loginDTO);

    /**
     * 登出业务（验证令牌，销毁登录状态）
     */
    LogoutResponseVO logout(String token);
}