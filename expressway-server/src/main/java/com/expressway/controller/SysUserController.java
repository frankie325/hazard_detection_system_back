package com.expressway.controller;

import com.expressway.context.BaseContext;
import com.expressway.dto.UserAddDTO;
import com.expressway.dto.UserQueryParamsDTO;
import com.expressway.dto.UserUpdateDTO;
import com.expressway.entity.SysUser;
import com.expressway.exception.UserException;
import com.expressway.result.Result;
import com.expressway.service.SysUserService;
import com.expressway.vo.UserVO;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;
    
    /**
     * 分页获取用户列表
     */
    @PostMapping("/list")
    public Result<PageInfo<UserVO>> getUserList(@Validated @RequestBody UserQueryParamsDTO queryParams) {
        PageInfo<UserVO> pageResult = sysUserService.getUserList(queryParams);
        return Result.success(pageResult);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<SysUser> getUserInfo() {
        Long userId = BaseContext.getCurrentId();
        SysUser user = sysUserService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public Result<?> addUser(@Validated @RequestBody UserAddDTO userAddDTO) {
        try {
            sysUserService.addUser(userAddDTO);
            return Result.success("新增用户成功");
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("新增用户失败：" + e.getMessage());
        }
    }

    /**
     * 编辑用户
     */
    @PutMapping("/update")
    public Result<?> updateUser(@Validated @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            sysUserService.updateUser(userUpdateDTO);
            return Result.success("编辑用户成功");
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("编辑用户失败：" + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteUser(@PathVariable("id") Long id) {
        try {
            sysUserService.deleteUser(id);
            return Result.success("删除用户成功");
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除用户失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batchDelete")
    public Result<?> batchDeleteUser(@RequestBody List<Long> ids) {
        try {
            sysUserService.batchDeleteUser(ids);
            return Result.success("批量删除用户成功");
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("批量删除用户失败：" + e.getMessage());
        }
    }
}
