package com.expressway.controller;

import com.expressway.dto.RoleAddDTO;
import com.expressway.dto.RoleUpdateDTO;
import com.expressway.dto.RoleQueryParamsDTO;
import com.expressway.entity.SysRole;
import com.expressway.exception.RoleException;
import com.expressway.result.Result;
import com.expressway.service.SysRoleService;
import com.expressway.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController()
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 获取所有角色列表
     * */
    @GetMapping("/allList")
    public Result<List<SysRole>> getAllRole() {
        List<SysRole> sysRoles = sysRoleService.getAllRole();
        return Result.success(sysRoles);
    }

    /**
     * 分页获取角色列表
     *
     * @param queryParams 查询参数
     * @return 统一响应结果
     */
    @PostMapping("/list")
    public Result<PageInfo<RoleVO>> getRoleList(@Validated @RequestBody RoleQueryParamsDTO queryParams) {
        PageInfo<RoleVO> pageResult = sysRoleService.getRoleList(queryParams);
        return Result.success(pageResult);
    }

    /**
     * 新增角色
     *
     * @param roleAddDTO 新增角色参数
     * @return 统一响应结果
     */
    @PostMapping("/add")
    public Result<?> addRole(@Validated @RequestBody RoleAddDTO roleAddDTO) {
        try {
            sysRoleService.addRole(roleAddDTO);
            return Result.success("新增角色成功");
        } catch (RoleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("新增角色失败：" + e.getMessage());
        }
    }

    /**
     * 编辑角色
     *
     * @param roleUpdateDTO 编辑角色参数
     * @return 统一响应结果
     */
    @PutMapping("/update")
    public Result<?> updateRole(@Validated @RequestBody RoleUpdateDTO roleUpdateDTO) {
        try {
            sysRoleService.updateRole(roleUpdateDTO);
            return Result.success("编辑角色成功");
        } catch (RoleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("编辑角色失败：" + e.getMessage());
        }
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 统一响应结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteRole(@PathVariable("id") Long id) {
        try {
            sysRoleService.deleteRole(id);
            return Result.success("删除角色成功");
        } catch (RoleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除角色失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色ID列表
     * @return 统一响应结果
     */
    @DeleteMapping("/batchDelete")
    public Result<?> batchDeleteRole(@RequestBody List<Long> ids) {
        try {
            sysRoleService.batchDeleteRole(ids);
            return Result.success("批量删除角色成功");
        } catch (RoleException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("批量删除角色失败：" + e.getMessage());
        }
    }
}
