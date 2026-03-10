package com.expressway.controller;

import com.expressway.result.Result;
import com.expressway.service.EmeReportService;
import com.expressway.vo.EmeEventVO;
import com.expressway.vo.EmeReportVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/emergency/report")
public class EmeReportController {

    @Resource
    private EmeReportService emeReportService;

    /**
     * 获取可用于生成报告的应急事件列表
     */
    @GetMapping("/events")
    public Result<List<EmeEventVO>> getReportEventList() {
        List<EmeEventVO> eventList = emeReportService.getReportEventList();
        return Result.success(eventList);
    }

    /**
     * 根据事件ID获取完整的报告数据
     */
    @GetMapping("/{eventId}")
    public Result<EmeReportVO> getReportByEventId(@PathVariable Long eventId) {
        EmeReportVO report = emeReportService.getReportByEventId(eventId);
        return Result.success(report);
    }

    /**
     * 生成报告分享链接
     */
    @PostMapping("/{eventId}/share")
    public Result<String> generateShareLink(@PathVariable Long eventId) {
        String shareLink = emeReportService.generateShareLink(eventId);
        return Result.success(shareLink);
    }

    /**
     * 导出报告PDF
     */
    @GetMapping("/{eventId}/export")
    public void exportReport(@PathVariable Long eventId, HttpServletResponse response) throws IOException {
        // 暂时作为占位，实际项目中需要实现PDF生成逻辑
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report_" + eventId + ".pdf");
        
        // 这里可以添加PDF生成逻辑
        // 例如使用iText等库生成PDF
        
        response.getWriter().write("PDF export functionality will be implemented in the future.");
        response.getWriter().flush();
    }
}