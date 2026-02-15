package com.expressway.mapper;

import com.expressway.dto.AreaQueryParamsDTO;
import com.expressway.entity.SysArea;
import com.expressway.vo.AreaVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysAreaMapper {
    /**
     * 查询所有区域
     */
    List<SysArea> selectAllArea();

    /**
     * 根据条件查询区域列表
     */
    List<AreaVO> selectAreaList(AreaQueryParamsDTO queryParams);

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
}
