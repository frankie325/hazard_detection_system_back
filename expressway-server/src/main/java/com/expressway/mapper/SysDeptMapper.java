package com.expressway.mapper;

import com.expressway.dto.DeptQueryParamsDTO;
import com.expressway.entity.SysDept;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SysDeptMapper {
    /**
     * 新增部门
     */
    int insertDept(SysDept sysDept);

    /**
     * 根据ID更新部门
     */
    int updateDeptById(SysDept sysDept);

    /**
     * 根据ID删除部门
     */
    int deleteDeptById(Long id);

    /**
     * 批量删除部门
     */
    int batchDeleteDept(@Param("ids") List<Long> ids);

    /**
     * 根据ID查询部门
     */
    SysDept selectDeptById(Long id);

    /**
     * 查询所有部门（平级列表）
     */
    List<SysDept> selectAllDept(DeptQueryParamsDTO deptQueryParamsDTO);

    /**
     * 根据上级部门ID查询子部门
     */
    List<SysDept> selectDeptByParentId(Long parentId);

    /**
     * 检查部门是否存在子部门
     */
    int countChildrenByParentId(Long parentId);
}