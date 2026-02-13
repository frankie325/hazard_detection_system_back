package com.expressway.service.impl;

import com.expressway.dto.LoginDTO;
import com.expressway.dto.UserAddDTO;
import com.expressway.dto.UserQueryParamsDTO;
import com.expressway.dto.UserUpdateDTO;
import com.expressway.entity.SysUser;
import com.expressway.exception.AuthException;
import com.expressway.exception.UserException;
import com.expressway.mapper.SysUserMapper;
import com.expressway.service.SysUserService;
import com.expressway.utils.JwtUtils;
import com.expressway.utils.PasswordUtils;
import com.expressway.vo.LogoutResponseVO;
import com.expressway.vo.UserLoginVO;
import com.expressway.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户业务实现
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

    /**
     * 分页查询用户列表
     */
    @Override
    public PageInfo<UserVO> getUserList(UserQueryParamsDTO queryParams) {
        PageHelper.startPage(queryParams.getCurrent(), queryParams.getSize());
        List<UserVO> userList = sysUserMapper.selectUserList(queryParams);
        return new PageInfo<>(userList);
    }

    /**
     * 新增用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserAddDTO userAddDTO) {
        // 1. 校验用户名是否已存在
        SysUser existUser = sysUserMapper.selectByUsername(userAddDTO.getUsername());
        if (existUser != null) {
            throw new UserException("用户名已存在");
        }

        // 2. 校验手机号是否已存在
        if (userAddDTO.getPhone() != null && !userAddDTO.getPhone().isEmpty()) {
            SysUser phoneUser = sysUserMapper.selectByPhone(userAddDTO.getPhone());
            if (phoneUser != null) {
                throw new UserException("手机号已被使用");
            }
        }

        // 3. 创建用户
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userAddDTO, user);
        // 密码加密
        user.setPassword(PasswordUtils.encrypt(userAddDTO.getPassword()));

        int result = sysUserMapper.insert(user);
        if (result != 1) {
            throw new UserException("新增用户失败");
        }
    }

    /**
     * 编辑用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        // 1. 校验用户是否存在
        SysUser existUser = sysUserMapper.selectById(userUpdateDTO.getId());
        if (existUser == null) {
            throw new UserException("用户不存在");
        }

        // 2. 如果修改了手机号，检查是否重复
        if (userUpdateDTO.getPhone() != null && !userUpdateDTO.getPhone().isEmpty()) {
            SysUser phoneUser = sysUserMapper.selectByPhone(userUpdateDTO.getPhone());
            if (phoneUser != null && !phoneUser.getId().equals(userUpdateDTO.getId())) {
                throw new UserException("手机号已被使用");
            }
        }

        // 3. 校验用户名不能重复
        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isEmpty()) {
            SysUser user = sysUserMapper.selectByUsername(userUpdateDTO.getUsername());
            if (user != null && !user.getId().equals(userUpdateDTO.getId())) {
                throw new UserException("用户名已被使用");
            }
        }

        // 4. 更新用户
        int result = sysUserMapper.update(userUpdateDTO);
        if (result == 0) {
            throw new UserException("编辑用户失败");
        }
    }

    /**
     * 删除用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        // 1. 校验用户是否存在
        SysUser existUser = sysUserMapper.selectById(id);
        if (existUser == null) {
            throw new UserException("用户不存在");
        }

        // 2. 删除用户
        int result = sysUserMapper.delete(id);
        if (result == 0) {
            throw new UserException("删除用户失败");
        }
    }

    /**
     * 批量删除用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUser(List<Long> ids) {
        // 1. 校验参数
        if (ids == null || ids.isEmpty()) {
            throw new UserException("用户ID列表不能为空");
        }

        // 2. 校验所有用户是否存在
        for (Long id : ids) {
            SysUser existUser = sysUserMapper.selectById(id);
            if (existUser == null) {
                throw new UserException("用户ID " + id + " 不存在");
            }
        }

        // 3. 批量删除用户
        int result = sysUserMapper.batchDelete(ids);
        if (result != ids.size()) {
            throw new UserException("批量删除用户失败");
        }
    }
}