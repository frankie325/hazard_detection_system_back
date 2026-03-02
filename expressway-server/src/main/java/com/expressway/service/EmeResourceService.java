package com.expressway.service;

import com.expressway.dto.EmeEventResourceBindDTO;
import com.expressway.vo.EmeResourceVO;

import java.util.List;

public interface EmeResourceService {
    /**
     * 查询所有资源池列表
     */
    List<EmeResourceVO> getEmeResourceList();

    /**
     * 绑定应急事件和资源
     */
    void bindEventResource(EmeEventResourceBindDTO bindDTO);
}
