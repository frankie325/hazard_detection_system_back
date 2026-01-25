package com.expressway.controller;


import com.expressway.entity.SysArea;
import com.expressway.result.Result;
import com.expressway.service.SysAreaService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * 区域管理控制器
 * 处理区域相关HTTP请求，遵循RESTful风格
 */
@RestController
@RequestMapping("/user/sys/area")
public class SysAreaController {
    @Resource
    private SysAreaService sysAreaService;


    /***
     * 查询区域列表数据
     * @return 区域列表数据
     */
    @GetMapping("/list")
    public Result<List<SysArea>> getAllList(){
        try{
            List <SysArea> areaList =  sysAreaService.getAllList();
            return Result.success(areaList);
        } catch (RuntimeException e) {
            return Result.error("查询区域列表数据：" + e.getMessage());
        }
    }
}
