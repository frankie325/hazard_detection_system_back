package com.expressway.controller;

import com.expressway.context.BaseContext;
import com.expressway.entity.SysUser;
import com.expressway.result.Result;
import com.expressway.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

//    @GetMapping("/user/list")
//    public List<SysUser> getUserList(){
//
//    }

    @GetMapping("/user/info")
    public Result<SysUser> getUserInfo(){
        Long userId = BaseContext.getCurrentId();
        SysUser user = sysUserService.getUserById(userId);
        return Result.success(user);
    }
}
