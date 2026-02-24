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
     * 创建应急事件
     */
    int insertEmeEvent(EmeEvent emeEvent);
}
