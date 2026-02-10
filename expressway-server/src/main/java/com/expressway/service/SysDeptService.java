package com.expressway.service;

import com.expressway.dto.DeptAddDTO;
import com.expressway.dto.DeptQueryParamsDTO;
import com.expressway.dto.DeptUpdateDTO;
import com.expressway.entity.SysDept;
import com.expressway.vo.DeptTreeVO;

import java.util.List;

public interface SysDeptService {
    /**
     * 新增部门
     */
    void addDept(DeptAddDTO deptAddDTO);

    /**
     * 编辑部门
     */
    void updateDept(DeptUpdateDTO deptUpdateDTO);

    /**
     * 根据ID删除部门（检查子部门）
     */
    void deleteDeptById(Long id);

    /**
     * 批量删除部门（检查子部门）
     */
    void batchDeleteDept(List<Long> ids);

    /**
     * 查询部门列表（平级）
     */
    List<SysDept> getAllDeptList(DeptQueryParamsDTO queryParams);

    /**
     * 查询部门树形结构
     * @param queryParams 部门名称、部门编码（可选，模糊查询）
     * @return 部门树形结构
     */
    List<DeptTreeVO> getDeptTree(DeptQueryParamsDTO queryParams);

    /**
     * 根据ID查询部门
     */
    SysDept getDeptById(Long id);
}