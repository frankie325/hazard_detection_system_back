package com.expressway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Python检测服务配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "python.detect.service")
public class PythonDetectConfig {

    /**
     * HTTP服务地址
     */
    private String url;

    /**
     * WebSocket服务地址
     */
    private String wsUrl;


    /**
     * 生成事件流记录的采样间隔，3s
     */
    private Integer eventStreamInterval = 3;

    /**
     * 获取启动会话完整URL
     */
    public String getSessionStartUrl() {
        return url + "/detect/session/start";
    }

    /**
     * 获取停止会话完整URL
     */
    public String getSessionStopUrl(String sessionId) {
        return url + "/detect/session/stop/" + sessionId;
    }

    /**
     * 获取WebSocket检测完整URL
     */
    public String getWsDetectUrl(String sessionId) {
        return wsUrl + "/ws/detect/" + sessionId;
    }
}
