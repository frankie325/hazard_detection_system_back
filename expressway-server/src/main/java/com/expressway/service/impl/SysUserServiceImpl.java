package com.expressway.service.impl;

import com.expressway.dto.LoginDTO;
import com.expressway.entity.SysUser;
import com.expressway.exception.AuthException;
import com.expressway.mapper.SysUserMapper;
import com.expressway.service.SysUserService;
import com.expressway.utils.JwtUtils;
import com.expressway.utils.PasswordUtils;
import com.expressway.vo.LogoutResponseVO;
import com.expressway.vo.UserLoginVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户认证业务实现
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private static final String SECRET_KEY = "expressWay";

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserLoginVO login(LoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        SysUser sysUser = sysUserMapper.selectByUsername(loginDTO.getUsername());
        if (sysUser == null) {
            throw new AuthException("用户不存在");
        }

        // 2. 验证密码（前端明文 -> 后端加密对比）
        boolean isPwdValid = PasswordUtils.verify(loginDTO.getPassword(), sysUser.getPassword());
        if (!isPwdValid) {
            throw new AuthException("用户名或密码错误");
        }

        // 3. 生成JWT令牌
        String token = JwtUtils.generateToken(SECRET_KEY, sysUser.getId(), sysUser.getUsername());

        // 4. 封装用户信息VO（拷贝实体属性，关联部门/角色名称）
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(sysUser, userLoginVO);
        userLoginVO.setToken(token);

        return userLoginVO;
    }

    @Override
    public LogoutResponseVO logout(String token) {
        // JWT无状态，登出核心是前端删除令牌；服务端可增加Redis黑名单机制（可选）
        LogoutResponseVO logoutResponseVO = new LogoutResponseVO();
        if (JwtUtils.validateToken(SECRET_KEY, token)) {
            logoutResponseVO.setSuccess(true);
            logoutResponseVO.setMessage("登出成功");
        } else {
            logoutResponseVO.setSuccess(false);
            logoutResponseVO.setMessage("令牌无效，无需登出");
        }
        return logoutResponseVO;
    }

    @Override
    public SysUser getUserById(Long userId) {
        return sysUserMapper.selectById(userId);
    }
}