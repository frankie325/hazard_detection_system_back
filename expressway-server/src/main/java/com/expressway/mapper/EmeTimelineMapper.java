package com.expressway.mapper;

import com.expressway.entity.EmeTimeline;
import com.expressway.vo.EmeTimelineVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmeTimelineMapper {
    /**
     * 创建时间线
     */
    int insertEmeTimeline(EmeTimeline emeTimeline);

    /**
     * 根据应急事件ID查询时间线列表
     */
    List<EmeTimelineVO> selectByEmergencyId(Long emergencyId);
}
