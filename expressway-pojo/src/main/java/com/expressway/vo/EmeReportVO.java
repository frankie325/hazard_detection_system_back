package com.expressway.vo;

import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import com.expressway.enumeration.EmeEventStatus;

import java.time.LocalDateTime;
import java.util.List;

public class EmeReportVO {
    private Long eventId;
    private String eventName;
    private DetectEventType eventType;
    private AlarmLevel eventLevel;
    private String location;
    private EmeEventStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateTime;
    
    // KPI指标
    private String responseTime; // 响应时间
    private String arrivalTime;  // 到场时间
    private String recoveryTime; // 恢复时间
    
    // 时间线
    private List<EmeTimelineVO> timeline;
    
    // 绑定的资源
    private List<EmeResourceVO> resources;
    
    // 资源绑定信息（包含绑定时间、路线等详细信息）
    private List<EmeResourceBindVO> resourceBindList;
    
    // 问题与改进建议
    private List<String> improvementSuggestions;
    
    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public DetectEventType getEventType() {
        return eventType;
    }
    public void setEventType(DetectEventType eventType) {
        this.eventType = eventType;
    }
    public AlarmLevel getEventLevel() {
        return eventLevel;
    }
    public void setEventLevel(AlarmLevel eventLevel) {
        this.eventLevel = eventLevel;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public EmeEventStatus getStatus() {
        return status;
    }
    public void setStatus(EmeEventStatus status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
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
    public List<EmeTimelineVO> getTimeline() {
        return timeline;
    }
    public void setTimeline(List<EmeTimelineVO> timeline) {
        this.timeline = timeline;
    }
    public List<EmeResourceVO> getResources() {
        return resources;
    }
    public void setResources(List<EmeResourceVO> resources) {
        this.resources = resources;
    }
    public List<EmeResourceBindVO> getResourceBindList() {
        return resourceBindList;
    }
    public void setResourceBindList(List<EmeResourceBindVO> resourceBindList) {
        this.resourceBindList = resourceBindList;
    }
    public List<String> getImprovementSuggestions() {
        return improvementSuggestions;
    }
    public void setImprovementSuggestions(List<String> improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }
}