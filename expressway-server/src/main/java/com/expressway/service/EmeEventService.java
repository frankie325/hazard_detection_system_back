package com.expressway.service;

import com.expressway.dto.EmeEventAddDTO;
import com.expressway.dto.EmeEventStatusUpdateDTO;
import com.expressway.dto.EmeTimelineAddDTO;
import com.expressway.vo.EmeEventVO;
import com.expressway.vo.EmeTimelineVO;

import java.util.List;

public interface EmeEventService {
    /**
     * 查询所有应急事件列表
     */
    List<EmeEventVO> getEmeEventList();

    /**
     * 创建应急事件
     */
    void createEmeEvent(EmeEventAddDTO addDTO);

    /**
     * 生成时间线
     */
    void createTimeline(EmeTimelineAddDTO timeline);

    /**
     * 更新事件状态
     */
    void updateEventStatus(EmeEventStatusUpdateDTO updateDTO);

    /**
     * 查询应急事件的时间线
     */
    List<EmeTimelineVO> getTimelineByEventId(Long eventId);

    /**
     * 根据ID查询事件详情
     */
    EmeEventVO getEventById(Long id);
}
