package com.expressway.service;

import com.expressway.config.AlarmWebSocketHandler;
import com.expressway.vo.AlarmMessageVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * 告警通知服务
 * 用于管理告警消息的实时推送
 */
@Service
public class AlarmNotificationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 推送告警消息
     * @param alarmMessage 告警消息
     */
    public void pushAlarmNotification(AlarmMessageVO alarmMessage) {
        try {
            // 将告警消息转换为JSON字符串
            String message = objectMapper.writeValueAsString(alarmMessage);
            
            // 广播消息给所有连接的客户端
            AlarmWebSocketHandler.broadcastAlarmMessage(message);
            
            System.out.println("已推送告警通知：" + alarmMessage.getAlarmName());
        } catch (Exception e) {
            System.err.println("推送告警通知失败：" + e.getMessage());
        }
    }

    /**
     * 推送告警状态变更通知
     * @param alarmId 告警ID
     * @param status 新状态
     */
    public void pushAlarmStatusUpdate(Long alarmId, String status) {
        try {
            // 构建状态变更消息
            String message = String.format("{\"type\":\"status_update\",\"alarmId\":%d,\"status\":\"%s\"}", alarmId, status);
            
            // 广播消息给所有连接的客户端
            AlarmWebSocketHandler.broadcastAlarmMessage(message);
            
            System.out.println("已推送告警状态变更通知：告警ID=" + alarmId + "，状态=" + status);
        } catch (Exception e) {
            System.err.println("推送告警状态变更通知失败：" + e.getMessage());
        }
    }
}
