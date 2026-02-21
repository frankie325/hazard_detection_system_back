package com.expressway.service;

import com.expressway.dto.DetectEventStreamQueryParamsDTO;
import com.expressway.vo.DetectEventStreamVO;
import com.github.pagehelper.PageInfo;

public interface DetectEventStreamService {
    /**
     * 分页查询事件流列表
     */
    PageInfo<DetectEventStreamVO> getEventStreamList(DetectEventStreamQueryParamsDTO queryParams);
}
