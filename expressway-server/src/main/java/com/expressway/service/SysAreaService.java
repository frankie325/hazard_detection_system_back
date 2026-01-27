package com.expressway.service;

import com.expressway.dto.AreaAddDTO;
import com.expressway.dto.AreaTreeDTO;
import com.expressway.dto.AreaUpdateDTO;
import com.expressway.entity.SysArea;

import java.util.List;

public interface SysAreaService {
    /**
     * 查询区域列表
     */
    List<SysArea> getAllList();

    /**
     * 查询区域树形结构
     */
    List<AreaTreeDTO> getAreaTree();

    /**
     * 新增区域
     */
    void addArea(AreaAddDTO areaAddDTO);

    /**
     * 编辑区域
     */
    void updateArea(AreaUpdateDTO areaUpdateDTO);

    /**
     * 根据ID删除区域
     */
    void deleteAreaById(Long id);

    /**
     * 批量删除区域
     */
    void batchDeleteArea(List<Long> ids);

    /**
     * 根据ID查询区域
     */
    SysArea getAreaById(Long id);
}
