package com.expressway.mapper;
import com.expressway.entity.SysArea;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAreaMapper {
    /**
     * 查询所有区域
     */
    List<SysArea> selectAllArea();

    /**
     * 根据ID查询区域
     */
    SysArea selectAreaById(Long id);

    /**
     * 新增区域
     */
    int insertArea(SysArea sysArea);

    /**
     * 更新区域
     */
    int updateAreaById(SysArea sysArea);

    /**
     * 根据ID删除区域
     */
    int deleteAreaById(Long id);

    /**
     * 批量删除区域
     */
    int batchDeleteArea(List<Long> ids);

    /**
     * 根据部门ID查询区域数量
     */
    int countAreaByDeptId(Long deptId);

    /**
     * 根据父ID查询子区域
     */
    List<SysArea> selectAreaByParentId(Long parentId);
}
