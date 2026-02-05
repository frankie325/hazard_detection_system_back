package com.expressway.controller;

import com.expressway.context.BaseContext;
import com.expressway.entity.SysUser;
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
    public SysUser getUserInfo(){
        Long userId = BaseContext.getCurrentId();
        return sysUserService.getUserById(userId);
    }
}
