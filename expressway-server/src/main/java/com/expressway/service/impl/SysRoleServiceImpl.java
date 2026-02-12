package com.expressway.service.impl;


import com.expressway.dto.RoleAddDTO;
import com.expressway.dto.RoleUpdateDTO;
import com.expressway.dto.RoleQueryParamsDTO;
import com.expressway.entity.SysRole;
import com.expressway.exception.RoleException;
import com.expressway.mapper.SysRoleMapper;
import com.expressway.service.SysRoleService;
import com.expressway.vo.RoleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 获取角色列表
     */
    @Override
    public PageInfo<RoleVO> getRoleList(RoleQueryParamsDTO queryParams) {
        PageHelper.startPage(queryParams.getCurrent(), queryParams.getSize());
        List<RoleVO> sysRoles = sysRoleMapper.selectAllRole(queryParams);
        return new PageInfo<>(sysRoles);
    }

    /**
     * 新增角色
     */
    @Override
    public void addRole(RoleAddDTO roleAddDTO) {
        // 角色名称不能重复
        SysRole existRole = sysRoleMapper.selectByName(roleAddDTO.getRoleName());
        if (existRole != null) {
            throw new RoleException("角色名称已存在");
        }

        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleAddDTO, role);
        int result = sysRoleMapper.insert(role);
        if (result != 1) {
            throw new RoleException("新增角色失败");
        }
    }

    /**
     * 编辑角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleUpdateDTO roleUpdateDTO) {
        // 1. 校验角色是否存在
        SysRole existRole = sysRoleMapper.selectById(roleUpdateDTO.getId());
        if (existRole == null) {
            throw new RoleException("角色不存在");
        }

        // 2. 如果修改了角色名称，检查是否重复
        if (roleUpdateDTO.getRoleName() != null && !roleUpdateDTO.getRoleName().isEmpty()) {
            SysRole nameExistRole = sysRoleMapper.selectByName(roleUpdateDTO.getRoleName());
            // 如果存在同名角色，且不是当前角色本身
            if (nameExistRole != null && !nameExistRole.getId().equals(roleUpdateDTO.getId())) {
                throw new RoleException("角色名称已存在");
            }
        }

        // 3. 更新角色
        int result = sysRoleMapper.update(roleUpdateDTO);
        if (result == 0) {
            throw new RoleException("编辑角色失败");
        }
    }

    /**
     * 删除角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        // 1. 校验角色是否存在
        SysRole existRole = sysRoleMapper.selectById(id);
        if (existRole == null) {
            throw new RoleException("角色不存在");
        }

        // 2. 删除角色
        int result = sysRoleMapper.delete(id);
        if (result == 0) {
            throw new RoleException("删除角色失败");
        }
    }

    /**
     * 批量删除角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRole(List<Long> ids) {
        // 1. 校验参数
        if (ids == null || ids.isEmpty()) {
            throw new RoleException("角色ID列表不能为空");
        }

        // 2. 校验所有角色是否存在
        for (Long id : ids) {
            SysRole existRole = sysRoleMapper.selectById(id);
            if (existRole == null) {
                throw new RoleException("角色ID " + id + " 不存在");
            }
        }

        // 3. 批量删除角色
        int result = sysRoleMapper.batchDelete(ids);
        if (result != ids.size()) {
            throw new RoleException("批量删除角色失败");
        }
    }
}
