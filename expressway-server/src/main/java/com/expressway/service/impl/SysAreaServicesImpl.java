package com.expressway.service.impl;

import com.expressway.dto.AreaAddDTO;
import com.expressway.dto.AreaQueryParamsDTO;
import com.expressway.dto.AreaUpdateDTO;
import com.expressway.entity.SysArea;
import com.expressway.entity.SysDept;
import com.expressway.exception.AreaException;
import com.expressway.mapper.SysAreaMapper;
import com.expressway.mapper.SysDeptMapper;
import com.expressway.service.SysAreaService;
import com.expressway.vo.AreaVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysAreaServicesImpl implements SysAreaService {
    @Resource
    private SysAreaMapper sysAreaMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询所有区域列表（不分页）
     */
    @Override
    public List<AreaVO> getAllAreaList() {
        return sysAreaMapper.selectAllArea();
    }

    /**
     * 分页查询区域列表
     */
    @Override
    public PageInfo<AreaVO> getAreaList(AreaQueryParamsDTO queryParams) {
        PageHelper.startPage(queryParams.getCurrent(), queryParams.getSize());
        List<AreaVO> areaList = sysAreaMapper.selectAreaList(queryParams);
        return new PageInfo<>(areaList);
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

        // 2. DTO转实体
        SysArea sysArea = new SysArea();
        BeanUtils.copyProperties(areaAddDTO, sysArea);

        // 3. 插入数据库
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

        // 3. DTO转实体
        SysArea sysArea = new SysArea();
        BeanUtils.copyProperties(areaUpdateDTO, sysArea);

        // 4. 更新数据库
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
