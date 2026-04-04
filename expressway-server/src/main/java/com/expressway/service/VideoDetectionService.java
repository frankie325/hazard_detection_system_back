package com.expressway.service;

import java.io.OutputStream;

/**
 * 视频检测服务接口
 */
public interface VideoDetectionService {

    /**
     * 启动MJPEG视频检测流
     * 自动管理会话生命周期，流结束后自动停止会话
     *
     * @param deviceId     设备ID
     * @param videoUrl     视频URL
     * @param outputStream 输出流（用于写入MJPEG帧）
     */
    void startMjpegStream(String deviceId, String videoUrl, OutputStream outputStream);

    /**
     * 获取Python检测服务地址
     */
    String getPythonServiceUrl();
}
