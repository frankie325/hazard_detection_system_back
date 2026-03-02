package com.expressway.mapper;

import com.expressway.entity.EmeEvent;
import com.expressway.vo.EmeEventVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmeEventMapper {
    /**
     * 查询所有应急事件列表
     */
    List<EmeEventVO> selectAllEmeEvent();

    /**
     * 根据部门ID查询应急事件列表
     */
    List<EmeEventVO> selectEmeEventByDeptId(Long deptId);

    /**
     * 创建应急事件
     */
    int insertEmeEvent(EmeEvent emeEvent);

    /**
     * 根据ID查询应急事件
     */
    EmeEvent selectById(Long id);

    /**
     * 根据ID查询应急事件详情
     */
    EmeEventVO selectVOById(Long id);

    /**
     * 更新应急事件
     */
    int updateEmeEvent(EmeEvent emeEvent);
}
