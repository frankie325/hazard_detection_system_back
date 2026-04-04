package com.expressway.service;

import com.expressway.dto.DetectEventStreamQueryParamsDTO;
import com.expressway.entity.DetectEventStream;
import com.expressway.vo.DetectEventStreamVO;
import com.github.pagehelper.PageInfo;

public interface DetectEventStreamService {
    /**
     * 分页查询事件流列表
     */
    PageInfo<DetectEventStreamVO> getEventStreamList(DetectEventStreamQueryParamsDTO queryParams);

    /**
     * 创建事件流
     */
    void createEventStream(DetectEventStream eventStream);
}
