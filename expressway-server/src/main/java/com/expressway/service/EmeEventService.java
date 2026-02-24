package com.expressway.service;

import com.expressway.dto.EmeEventAddDTO;
import com.expressway.vo.EmeEventVO;

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
}
