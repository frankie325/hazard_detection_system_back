package com.expressway.controller;

import com.expressway.result.Result;
import com.expressway.service.VideoDetectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 视频检测控制器
 */
@Slf4j
@RestController
@RequestMapping("/video/detect")
@Api(tags = "视频检测接口")
public class VideoDetectionController {

    @Resource
    private VideoDetectionService videoDetectionService;

    /**
     * MJPEG视频检测流接口
     * 返回MJPEG格式视频流，前端可直接用<img>标签显示
     *
     * @param deviceId 设备ID
     * @param videoUrl 视频URL
     * @param response HTTP响应
     */
    @GetMapping(value = "/stream", produces = "multipart/x-mixed-replace; boundary=frame")
    @ApiOperation("MJPEG视频检测流接口")
    public void detectStream(
            @ApiParam(value = "设备ID", required = true)
            @RequestParam("deviceId") Long deviceId,
            @ApiParam(value = "视频URL", required = true)
            @RequestParam("videoUrl") String videoUrl,
            HttpServletResponse response) {

        log.info("接收到视频检测请求: deviceId={}, videoUrl={}", deviceId, videoUrl);

        // 设置MJPEG响应头
        response.setContentType("multipart/x-mixed-replace; boundary=frame");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Connection", "keep-alive");

        try {
            OutputStream outputStream = response.getOutputStream();
            videoDetectionService.startMjpegStream(deviceId, videoUrl, outputStream);
        } catch (IOException e) {
            log.debug("客户端断开连接: {}", e.getMessage());
        } catch (Exception e) {
            log.error("视频检测流异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取Python检测服务地址
     *
     * @return Python服务地址
     */
    @GetMapping("/service-url")
    @ApiOperation("获取检测服务地址")
    public Result<String> getServiceUrl() {
        return Result.success(videoDetectionService.getPythonServiceUrl());
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    @ApiOperation("检测服务健康检查")
    public Result<String> healthCheck() {
        return Result.success("Python检测服务地址: " + videoDetectionService.getPythonServiceUrl());
    }
}
