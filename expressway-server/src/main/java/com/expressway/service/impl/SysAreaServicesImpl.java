package com.expressway.service.impl;

import com.expressway.dto.AreaAddDTO;
import com.expressway.dto.AreaTreeDTO;
import com.expressway.dto.AreaUpdateDTO;
import com.expressway.entity.SysArea;
import com.expressway.entity.SysDept;
import com.expressway.exception.AreaException;
import com.expressway.mapper.SysAreaMapper;
import com.expressway.mapper.SysDeptMapper;
import com.expressway.service.SysAreaService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysAreaServicesImpl implements SysAreaService {
    @Resource
    private SysAreaMapper sysAreaMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询区域列表
     */
    @Override
    public List<SysArea> getAllList() {
        return sysAreaMapper.selectAllArea();
    }

    /**
     * 查询区域树形结构
     */
    @Override
    public List<AreaTreeDTO> getAreaTree() {
        List<SysArea> allAreas = sysAreaMapper.selectAllArea();
        return buildAreaTree(allAreas, 0L);
    }

    private List<AreaTreeDTO> buildAreaTree(List<SysArea> allAreas, Long parentId) {
        List<AreaTreeDTO> tree = new ArrayList<>();
        for (SysArea area : allAreas) {
            if (parentId.equals(area.getParentId())) {
                AreaTreeDTO node = new AreaTreeDTO();
                BeanUtils.copyProperties(area, node);
                List<AreaTreeDTO> children = buildAreaTree(allAreas, area.getId());
                if (!children.isEmpty()) {
                    node.setChildren(children);
                }
                tree.add(node);
            }
        }
        return tree;
    }

    /**
     * 新增区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArea(AreaAddDTO areaAddDTO) {
        // 1. 校验负责部门是否存在
        SysDept dept = sysDeptMapper.selectDeptById(areaAddDTO.getDeptId());
        if (dept == null) {
            throw new AreaException("负责部门不存在");
        }

        // 2. 校验上级区域是否存在（如果指定了parentId且不为0）
        if (areaAddDTO.getParentId() != null && !areaAddDTO.getParentId().equals(0L)) {
            SysArea parentArea = sysAreaMapper.selectAreaById(areaAddDTO.getParentId());
            if (parentArea == null) {
                throw new AreaException("上级区域不存在");
            }
        }

        // 3. DTO转实体
        SysArea sysArea = new SysArea();
        BeanUtils.copyProperties(areaAddDTO, sysArea);
        if (sysArea.getParentId() == null) {
            sysArea.setParentId(0L);
        }

        // 4. 插入数据库
        int insertResult = sysAreaMapper.insertArea(sysArea);
        if (insertResult != 1) {
            throw new AreaException("新增区域失败");
        }
    }

    /**
     * 编辑区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArea(AreaUpdateDTO areaUpdateDTO) {
        // 1. 校验区域是否存在
        SysArea existArea = sysAreaMapper.selectAreaById(areaUpdateDTO.getId());
        if (existArea == null) {
            throw new AreaException("区域不存在");
        }

        // 2. 校验负责部门是否存在
        SysDept dept = sysDeptMapper.selectDeptById(areaUpdateDTO.getDeptId());
        if (dept == null) {
            throw new AreaException("负责部门不存在");
        }

        // 3. 校验上级区域是否存在（如果指定了parentId且不为0）
        if (areaUpdateDTO.getParentId() != null && !areaUpdateDTO.getParentId().equals(0L)) {
            SysArea parentArea = sysAreaMapper.selectAreaById(areaUpdateDTO.getParentId());
            if (parentArea == null) {
                throw new AreaException("上级区域不存在");
            }
        }

        // 4. DTO转实体
        SysArea sysArea = new SysArea();
        BeanUtils.copyProperties(areaUpdateDTO, sysArea);
        if (sysArea.getParentId() == null) {
            sysArea.setParentId(0L);
        }

        // 5. 更新数据库
        int updateResult = sysAreaMapper.updateAreaById(sysArea);
        if (updateResult != 1) {
            throw new AreaException("编辑区域失败");
        }
    }

    /**
     * 根据ID删除区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAreaById(Long id) {
        // 1. 校验区域是否存在
        SysArea existArea = sysAreaMapper.selectAreaById(id);
        if (existArea == null) {
            throw new AreaException("区域不存在");
        }

        // 2. 执行删除
        int deleteResult = sysAreaMapper.deleteAreaById(id);
        if (deleteResult != 1) {
            throw new AreaException("删除区域失败");
        }
    }

    /**
     * 批量删除区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteArea(List<Long> ids) {
        // 1. 校验选择的数据
        if (ids == null || ids.isEmpty()) {
            throw new AreaException("请选择需要删除的数据！");
        }

        // 2. 逐个检查区域是否存在
        for (Long id : ids) {
            SysArea area = sysAreaMapper.selectAreaById(id);
            if (area == null) {
                throw new AreaException("区域ID：" + id + " 不存在，批量删除失败");
            }
        }

        // 3. 批量执行删除
        int deleteResult = sysAreaMapper.batchDeleteArea(ids);
        if (deleteResult == 0) {
            throw new AreaException("批量删除区域失败");
        }
    }

    /**
     * 根据ID查询区域
     */
    @Override
    public SysArea getAreaById(Long id) {
        if (id == null) {
            throw new AreaException("区域ID不能为空");
        }
        return sysAreaMapper.selectAreaById(id);
    }
}
