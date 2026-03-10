package com.expressway.service.impl;

import com.expressway.context.BaseContext;
import com.expressway.entity.EmeEvent;
import com.expressway.entity.EmeTimeline;
import com.expressway.enumeration.ActionType;
import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.EmeEventStatus;
import com.expressway.exception.EmeEventException;
import com.expressway.mapper.EmeEventMapper;
import com.expressway.mapper.EmeEventResourceMapper;
import com.expressway.mapper.EmeResourceMapper;
import com.expressway.mapper.EmeTimelineMapper;
import com.expressway.service.EmeReportService;
import com.expressway.vo.EmeEventVO;
import com.expressway.vo.EmeReportVO;
import com.expressway.vo.EmeResourceBindVO;
import com.expressway.vo.EmeResourceVO;
import com.expressway.vo.EmeTimelineVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmeReportServiceImpl implements EmeReportService {

    @Resource
    private EmeEventMapper emeEventMapper;
    
    @Resource
    private EmeTimelineMapper emeTimelineMapper;
    
    @Resource
    private EmeResourceMapper emeResourceMapper;
    
    @Resource
    private EmeEventResourceMapper emeEventResourceMapper;

    @Override
    public List<EmeEventVO> getReportEventList() {
        // 获取当前登录用户ID
        Long currentUserId = BaseContext.getCurrentId();
        if (currentUserId == null) {
            throw new EmeEventException("用户未登录");
        }
        
        // 这里可以根据用户权限过滤事件
        // 暂时返回所有事件
        return emeEventMapper.selectAllEmeEvent();
    }

    @Override
    public EmeReportVO getReportByEventId(Long eventId) {
        if (eventId == null) {
            throw new EmeEventException("事件ID不能为空");
        }
        
        // 1. 获取事件基本信息
        EmeEventVO eventVO = emeEventMapper.selectVOById(eventId);
        if (eventVO == null) {
            throw new EmeEventException("事件不存在");
        }
        
        // 2. 获取时间线
        List<EmeTimelineVO> timeline = emeTimelineMapper.selectByEmergencyId(eventId);
        
        // 3. 获取绑定的资源
        List<Long> resourceIds = emeEventResourceMapper.selectResourceIdsByEventId(eventId);
        List<EmeResourceVO> resources = new ArrayList<>();
        if (!resourceIds.isEmpty()) {
            resources = emeResourceMapper.selectByIds(resourceIds);
        }
        
        // 3.1 构建资源绑定信息（从时间线中提取）
        List<EmeResourceBindVO> resourceBindList = buildResourceBindInfo(resources, timeline);
        
        // 4. 计算KPI指标
        KpiResult kpiResult = calculateKPI(timeline, eventVO.getCreateTime());
        
        // 5. 生成改进建议
        List<String> suggestions = generateImprovementSuggestions(kpiResult);
        
        // 6. 构建报告VO
        EmeReportVO reportVO = new EmeReportVO();
        reportVO.setEventId(eventVO.getId());
        reportVO.setEventName(eventVO.getEventName());
        reportVO.setEventType(eventVO.getEventType());
        reportVO.setEventLevel(eventVO.getEventLevel());
        reportVO.setLocation(eventVO.getLocation());
        reportVO.setStatus(eventVO.getStatus());
        reportVO.setCreatedAt(eventVO.getCreateTime());
        reportVO.setUpdateTime(eventVO.getUpdateTime());
        reportVO.setTimeline(timeline);
        reportVO.setResources(resources);
        reportVO.setResourceBindList(resourceBindList);
        reportVO.setResponseTime(kpiResult.getResponseTime());
        reportVO.setArrivalTime(kpiResult.getArrivalTime());
        reportVO.setRecoveryTime(kpiResult.getRecoveryTime());
        reportVO.setImprovementSuggestions(suggestions);
        
        return reportVO;
    }

    @Override
    public String generateShareLink(Long eventId) {
        if (eventId == null) {
            throw new EmeEventException("事件ID不能为空");
        }
        
        // 验证事件是否存在
        EmeEvent event = emeEventMapper.selectById(eventId);
        if (event == null) {
            throw new EmeEventException("事件不存在");
        }
        
        // 生成分享链接（实际项目中应该包含token等安全信息）
        return "/api/emergency/report/share/" + eventId + "?token=" + generateToken(eventId);
    }

    /**
     * 构建资源绑定信息
     */
    private List<EmeResourceBindVO> buildResourceBindInfo(List<EmeResourceVO> resources, List<EmeTimelineVO> timeline) {
        List<EmeResourceBindVO> result = new ArrayList<>();
        
        for (EmeResourceVO resource : resources) {
            EmeResourceBindVO bindVO = new EmeResourceBindVO();
            bindVO.setId(resource.getId());
            bindVO.setResourceName(resource.getResourceName());
            bindVO.setResourceCode(resource.getResourceCode());
            bindVO.setResourceType(resource.getResourceType());
            bindVO.setQuantity(resource.getQuantity());
            bindVO.setStatus(resource.getStatus());
            
            // 从时间线中提取绑定信息
            for (EmeTimelineVO tl : timeline) {
                if (tl.getActionType() == ActionType.资源绑定 && tl.getActionText() != null && tl.getActionText().contains(resource.getResourceName())) {
                    bindVO.setBoundAt(tl.getOperateTime());
                    bindVO.setDeparture(tl.getDeparture());
                    bindVO.setDestination(tl.getDestination());
                    bindVO.setNote(tl.getRemark());
                    break;
                }
            }
            
            // 统计绑定次数和更新次数
            int bindCount = 0;
            int updateCount = 0;
            for (EmeTimelineVO tl : timeline) {
                if (tl.getActionType() == ActionType.资源绑定 && tl.getActionText() != null && tl.getActionText().contains(resource.getResourceName())) {
                    bindCount++;
                }
                if (tl.getActionType() == ActionType.行动备注 && tl.getActionText() != null && tl.getActionText().contains(resource.getResourceName())) {
                    updateCount++;
                }
            }
            bindVO.setBindCount(bindCount);
            bindVO.setUpdateCount(updateCount);
            
            result.add(bindVO);
        }
        
        return result;
    }

    /**
     * 计算KPI指标
     */
    private KpiResult calculateKPI(List<EmeTimelineVO> timeline, LocalDateTime createdAt) {
        KpiResult result = new KpiResult();
        
        LocalDateTime confirmedTime = null;
        LocalDateTime arrivalTime = null;
        LocalDateTime closedTime = null;
        
        // 从时间线中提取关键时间点
        for (EmeTimelineVO item : timeline) {
            if (item.getActionType() == ActionType.确认) {
                confirmedTime = item.getOperateTime();
            } else if (item.getActionType() == ActionType.现场签到) {
                arrivalTime = item.getOperateTime();
            } else if (item.getActionType() == ActionType.关闭事件) {
                closedTime = item.getOperateTime();
            }
        }
        
        // 计算响应时间（从事件创建到确认）
        if (confirmedTime != null && createdAt != null) {
            Duration duration = Duration.between(createdAt, confirmedTime);
            result.setResponseTime(formatDuration(duration));
        }
        
        // 计算到场时间（从确认到签到）
        if (arrivalTime != null && confirmedTime != null) {
            Duration duration = Duration.between(confirmedTime, arrivalTime);
            result.setArrivalTime(formatDuration(duration));
        }
        
        // 计算恢复时间（从签到到关闭）
        if (closedTime != null && arrivalTime != null) {
            Duration duration = Duration.between(arrivalTime, closedTime);
            result.setRecoveryTime(formatDuration(duration));
        }
        
        return result;
    }

    /**
     * 格式化时间 duration
     */
    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        
        if (hours > 0) {
            return hours + "h " + minutes + "min " + secs + "s";
        } else if (minutes > 0) {
            return minutes + "min " + secs + "s";
        } else {
            return secs + "s";
        }
    }

    /**
     * 生成改进建议
     */
    private List<String> generateImprovementSuggestions(KpiResult kpiResult) {
        List<String> suggestions = new ArrayList<>();
        
        // 根据响应时间生成建议
        if (kpiResult.getResponseTime() != null) {
            if (kpiResult.getResponseTime().contains("h") || 
                (kpiResult.getResponseTime().contains("min") && 
                 Integer.parseInt(kpiResult.getResponseTime().split("min")[0].trim()) > 15)) {
                suggestions.add("响应用时偏长，建议优化确认流程与告警通知链。");
            }
        }
        
        // 根据到场时间生成建议
        if (kpiResult.getArrivalTime() != null) {
            if (kpiResult.getArrivalTime().contains("h") || 
                (kpiResult.getArrivalTime().contains("min") && 
                 Integer.parseInt(kpiResult.getArrivalTime().split("min")[0].trim()) > 30)) {
                suggestions.add("到场耗时较长，建议复核路线规划与资源就近策略。");
            }
        }
        
        // 根据恢复时间生成建议
        if (kpiResult.getRecoveryTime() != null) {
            if (kpiResult.getRecoveryTime().contains("h") || 
                (kpiResult.getRecoveryTime().contains("min") && 
                 Integer.parseInt(kpiResult.getRecoveryTime().split("min")[0].trim()) > 60)) {
                suggestions.add("恢复阶段较慢，建议梳理处置环节与瓶颈资源。");
            }
        }
        
        // 如果没有建议，添加默认建议
        if (suggestions.isEmpty()) {
            suggestions.add("暂无明显瓶颈；保持现有联动与处置节奏。");
        }
        
        return suggestions;
    }

    /**
     * 生成分享链接的token（简化版）
     */
    private String generateToken(Long eventId) {
        // 实际项目中应该使用更安全的token生成方式
        return "token_" + eventId + "_" + System.currentTimeMillis();
    }

    /**
     * KPI计算结果
     */
    private static class KpiResult {
        private String responseTime;
        private String arrivalTime;
        private String recoveryTime;
        
        public String getResponseTime() {
            return responseTime;
        }
        public void setResponseTime(String responseTime) {
            this.responseTime = responseTime;
        }
        public String getArrivalTime() {
            return arrivalTime;
        }
        public void setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
        }
        public String getRecoveryTime() {
            return recoveryTime;
        }
        public void setRecoveryTime(String recoveryTime) {
            this.recoveryTime = recoveryTime;
        }
    }
}