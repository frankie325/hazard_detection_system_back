package com.expressway.service;

import com.expressway.dto.AreaAddDTO;
import com.expressway.dto.AreaQueryParamsDTO;
import com.expressway.dto.AreaUpdateDTO;
import com.expressway.entity.SysArea;
import com.expressway.vo.AreaVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SysAreaService {
    /**
     * 分页查询区域列表
     */
    PageInfo<AreaVO> getAreaList(AreaQueryParamsDTO queryParams);

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
