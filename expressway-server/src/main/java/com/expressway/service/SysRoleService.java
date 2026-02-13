package com.expressway.service;

import com.expressway.dto.RoleAddDTO;
import com.expressway.dto.RoleUpdateDTO;
import com.expressway.dto.RoleQueryParamsDTO;
import com.expressway.entity.SysRole;
import com.expressway.vo.RoleVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SysRoleService {

    /**
     * 获取所有角色列表
     */
    List<SysRole> getAllRole();
    /**
     * 分页获取角色列表
     */
    PageInfo<RoleVO> getRoleList(RoleQueryParamsDTO queryParams);

    /**
     * 新增角色
     */
    void addRole(RoleAddDTO roleAddDTO);

    /**
     * 编辑角色
     */
    void updateRole(RoleUpdateDTO roleUpdateDTO);

    /**
     * 删除角色
     */
    void deleteRole(Long id);

    /**
     * 批量删除角色
     */
    void batchDeleteRole(List<Long> ids);
}
