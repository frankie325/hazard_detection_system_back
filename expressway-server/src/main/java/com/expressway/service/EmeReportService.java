package com.expressway.service;

import com.expressway.vo.EmeEventVO;
import com.expressway.vo.EmeReportVO;

import java.util.List;

public interface EmeReportService {
    /**
     * 获取可用于生成报告的应急事件列表
     */
    List<EmeEventVO> getReportEventList();

    /**
     * 根据事件ID获取完整的报告数据
     */
    EmeReportVO getReportByEventId(Long eventId);

    /**
     * 生成报告分享链接
     */
    String generateShareLink(Long eventId);
}