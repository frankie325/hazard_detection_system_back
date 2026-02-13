package com.expressway.mapper;

import com.expressway.dto.UserQueryParamsDTO;
import com.expressway.dto.UserUpdateDTO;
import com.expressway.entity.SysUser;
import com.expressway.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据访问接口
 */
@Mapper
public interface SysUserMapper {
    /**
     * 根据ID查询用户
     */
    @Select("select * from sys_user where id = #{id}")
    SysUser selectById(Long id);

    /**
     * 根据用户名查询用户
     */
    @Select("select * from sys_user where username = #{username}")
    SysUser selectByUsername(String username);

    /**
     * 根据手机号查询用户
     */
    @Select("select * from sys_user where phone = #{phone}")
    SysUser selectByPhone(String phone);

    /**
     * 分页查询用户列表
     */
    List<UserVO> selectUserList(UserQueryParamsDTO queryParams);

    /**
     * 新增用户
     */
    int insert(SysUser user);

    /**
     * 更新用户
     */
    int update(UserUpdateDTO userUpdateDTO);

    /**
     * 删除用户
     */
    int delete(Long id);

    /**
     * 批量删除用户
     */
    int batchDelete(List<Long> ids);
}
