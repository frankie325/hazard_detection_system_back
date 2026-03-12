package com.expressway.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 告警WebSocket处理器
 * 用于实时推送告警消息给前端
 */
public class AlarmWebSocketHandler extends TextWebSocketHandler {

    // 存储所有活跃的WebSocket会话
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 新连接建立时添加到会话列表
        sessions.add(session);
        System.out.println("新的WebSocket连接已建立，当前连接数：" + sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接关闭时从会话列表中移除
        sessions.remove(session);
        System.out.println("WebSocket连接已关闭，当前连接数：" + sessions.size());
    }

    /**
     * 广播告警消息给所有连接的客户端
     * @param message 告警消息
     */
    public static void broadcastAlarmMessage(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    System.err.println("发送告警消息失败：" + e.getMessage());
                }
            }
        }
    }

    /**
     * 发送消息给指定客户端
     * @param session WebSocket会话
     * @param message 消息内容
     */
    public static void sendMessage(WebSocketSession session, String message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                System.err.println("发送消息失败：" + e.getMessage());
            }
        }
    }
}
