package com.expressway.mapper;

import com.expressway.dto.RoleUpdateDTO;
import com.expressway.dto.RoleQueryParamsDTO;
import com.expressway.entity.SysRole;
import com.expressway.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 角色数据访问接口
 */
@Mapper
public interface SysRoleMapper {
    // 获取角色总数
    Long count();

    // 获取所有角色
    @Select("select * from sys_role")
    List<SysRole> selectAll();

    // 获取角色列表
    List<RoleVO> selectRoleList(RoleQueryParamsDTO roleQueryParamsDTO);

    @Select("select * from sys_role where role_name = #{roleName}")
    SysRole selectByName(String roleName);

    @Select("select * from sys_role where id = #{id}")
    SysRole selectById(Long id);

    // 新增角色
    int insert(SysRole role);

    // 更新角色
    int update(RoleUpdateDTO roleUpdateDTO);

    // 删除角色
    int delete(Long id);

    // 批量删除角色
    int batchDelete(List<Long> ids);
}
