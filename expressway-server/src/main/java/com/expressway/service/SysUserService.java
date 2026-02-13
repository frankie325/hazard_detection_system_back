package com.expressway.service;


import com.expressway.dto.LoginDTO;
import com.expressway.dto.UserAddDTO;
import com.expressway.dto.UserQueryParamsDTO;
import com.expressway.dto.UserUpdateDTO;
import com.expressway.entity.SysUser;
import com.expressway.vo.LogoutResponseVO;
import com.expressway.vo.UserLoginVO;
import com.expressway.vo.UserVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 用户相关业务接口
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

    /**
     * 根据用户ID查询用户信息
     */
    SysUser getUserById(Long userId);

    /**
     * 分页查询用户列表
     */
    PageInfo<UserVO> getUserList(UserQueryParamsDTO queryParams);

    /**
     * 新增用户
     */
    void addUser(UserAddDTO userAddDTO);

    /**
     * 编辑用户
     */
    void updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 批量删除用户
     */
    void batchDeleteUser(List<Long> ids);
}