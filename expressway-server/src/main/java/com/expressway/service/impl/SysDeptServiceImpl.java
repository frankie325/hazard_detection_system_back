package com.expressway.service.impl;

import com.expressway.dto.DeptAddDTO;
import com.expressway.dto.DeptQueryParamsDTO;
import com.expressway.dto.DeptUpdateDTO;
import com.expressway.entity.SysDept;
import com.expressway.exception.DeptException;
import com.expressway.mapper.SysDeptMapper;
import com.expressway.service.SysDeptService;
import com.expressway.vo.DeptTreeVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysDeptServiceImpl implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 新增部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDept(DeptAddDTO deptAddDTO) {
        // 1. 校验上级部门是否存在（parentId非0时）
        if (deptAddDTO.getParentId() != 0) {
            SysDept parentDept = sysDeptMapper.selectDeptById(deptAddDTO.getParentId());
            if (parentDept == null) {
                throw new DeptException("上级部门不存在");
            }
        }

        // 2. DTO转实体
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(deptAddDTO, sysDept);

        // 3. 插入数据库
        int insertResult = sysDeptMapper.insertDept(sysDept);
        if (insertResult != 1) {
            throw new DeptException("新增部门失败");
        }
    }

    /**
     * 编辑部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(DeptUpdateDTO deptUpdateDTO) {
        // 1. 校验部门是否存在
        SysDept existDept = sysDeptMapper.selectDeptById(deptUpdateDTO.getId());
        if (existDept == null) {
            throw new DeptException("部门不存在");
        }

        // 2. 校验上级部门（parentId非0时）
        if (deptUpdateDTO.getParentId() != 0) {
            SysDept parentDept = sysDeptMapper.selectDeptById(deptUpdateDTO.getParentId());
            if (parentDept == null) {
                throw new DeptException("上级部门不存在");
            }
            // 禁止自关联（上级部门ID不能等于自身ID）
            if (deptUpdateDTO.getParentId().equals(deptUpdateDTO.getId())) {
                throw new DeptException("上级部门不能选择自身");
            }
            // 禁止循环关联（避免A→B→A的层级闭环）
            if (isCircularReference(deptUpdateDTO.getId(), deptUpdateDTO.getParentId())) {
                throw new DeptException("上级部门选择导致循环关联，请重新选择");
            }
        }

        // 3. DTO转实体
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(deptUpdateDTO, sysDept);

        // 4. 更新数据库
        int updateResult = sysDeptMapper.updateDeptById(sysDept);
        if (updateResult != 1) {
            throw new DeptException("编辑部门失败");
        }
    }

    /**
     * 单个删除部门（严格遵循树形删除规则）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeptById(Long id) {
        // 1. 校验部门是否存在
        SysDept existDept = sysDeptMapper.selectDeptById(id);
        if (existDept == null) {
            throw new DeptException("部门不存在");
        }

        // 2. 检查是否存在子部门（核心规则）
        int childrenCount = sysDeptMapper.countChildrenByParentId(id);
        if (childrenCount > 0) {
            throw new DeptException("请先删除子部门！");
        }

        // 3. 执行删除
        int deleteResult = sysDeptMapper.deleteDeptById(id);
        if (deleteResult != 1) {
            throw new DeptException("删除部门失败");
        }
    }

    /**
     * 批量删除部门（支持头部批量操作）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteDept(List<Long> ids) {
        // 1. 校验选择的数据
        if (ids == null || ids.isEmpty()) {
            throw new DeptException("请选择需要删除的数据！");
        }

        // 2. 逐个检查部门状态（存在性+子部门）
        for (Long id : ids) {
            SysDept dept = sysDeptMapper.selectDeptById(id);
            if (dept == null) {
                throw new DeptException("部门ID：" + id + " 不存在，批量删除失败");
            }
            int childrenCount = sysDeptMapper.countChildrenByParentId(id);
            if (childrenCount > 0) {
                throw new DeptException("部门【" + dept.getDeptName() + "】存在子部门，请先删除子部门！");
            }
        }

        // 3. 批量执行删除
        int deleteResult = sysDeptMapper.batchDeleteDept(ids);
        if (deleteResult == 0) {
            throw new DeptException("批量删除部门失败");
        }
    }

    /**
     * 查询平级部门列表（用于表格展示）
     */
    @Override
    public List<SysDept> getAllDeptList(DeptQueryParamsDTO queryParams) {
        return sysDeptMapper.selectAllDept(queryParams);
    }

    /**
     * 查询部门树形结构（用于树形展示+新增子部门时的上级选择）
     */
    @Override
    public List<DeptTreeVO> getDeptTree(DeptQueryParamsDTO queryParams) {
        // 1. 查询所有部门数据
        List<SysDept> allDept = sysDeptMapper.selectAllDept(queryParams);
        // 2. 递归构建树形结构（根节点parentId=0）
        return buildDeptTree(allDept, 0L);
    }

    /**
     * 根据ID查询单个部门（用于编辑回显）
     */
    @Override
    public SysDept getDeptById(Long id) {
        if (id == null) {
            throw new DeptException("部门ID不能为空");
        }
        return sysDeptMapper.selectDeptById(id);
    }

    /**
     * 递归构建部门树形结构
     * @param allDept 所有部门列表
     * @param parentId 上级部门ID（递归入口为0）
     * @return 树形结构VO列表
     */
    private List<DeptTreeVO> buildDeptTree(List<SysDept> allDept, Long parentId) {
        List<DeptTreeVO> treeVOList = new ArrayList<>();

        // 筛选当前上级部门的所有子部门
        List<SysDept> childrenDept = allDept.stream()
                .filter(dept -> parentId.equals(dept.getParentId()))
                .collect(Collectors.toList());

        // 递归构建子层级
        for (SysDept dept : childrenDept) {
            DeptTreeVO treeVO = new DeptTreeVO();
            BeanUtils.copyProperties(dept, treeVO);
            // 递归查询当前部门的子部门
            List<DeptTreeVO> subTree = buildDeptTree(allDept, dept.getId());
            treeVO.setChildren(subTree);
            treeVOList.add(treeVO);
        }

        return treeVOList;
    }

    /**
     * 检查是否存在循环关联（防止A→B→A的层级闭环）
     * @param currentId 当前部门ID
     * @param parentId 上级部门ID
     * @return true-存在循环，false-正常
     */
    private boolean isCircularReference(Long currentId, Long parentId) {
        // 递归查询上级部门的上级，直到根节点（parentId=0）
        SysDept parentDept = sysDeptMapper.selectDeptById(parentId);
        if (parentDept == null) {
            return false;
        }
        // 如果上级部门的上级是当前部门，说明存在循环
        if (currentId.equals(parentDept.getParentId())) {
            return true;
        }
        // 继续向上追溯（直到根节点）
        return isCircularReference(currentId, parentDept.getParentId());
    }
}
