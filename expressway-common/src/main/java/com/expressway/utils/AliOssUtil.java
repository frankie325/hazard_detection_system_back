package com.expressway.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云OSS工具类
 */
@Slf4j
public class AliOssUtil {

    private final OSS ossClient;
    private final String endpoint;
    private final String bucketName;

    /**
     * 构造函数
     *
     * @param endpoint        OSS endpoint，如 oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId     AccessKey ID
     * @param accessKeySecret AccessKey Secret
     * @param bucketName      Bucket名称
     */
    public AliOssUtil(String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
        this.endpoint = endpoint;
        this.bucketName = bucketName;
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        log.info("OSS Client 初始化成功, endpoint: {}, bucket: {}", endpoint, bucketName);
    }

    /**
     * 文件上传
     *
     * @param bytes      文件字节数组
     * @param objectName 对象名称（存储路径）
     * @return 文件访问URL
     */
    public String upload(byte[] bytes, String objectName) {
        return upload(bytes, objectName, null);
    }

    /**
     * 文件上传（指定Content-Type）
     *
     * @param bytes       文件字节数组
     * @param objectName  对象名称（存储路径）
     * @param contentType 内容类型，如 image/jpeg
     * @return 文件访问URL
     */
    public String upload(byte[] bytes, String objectName, String contentType) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            if (contentType != null && !contentType.isEmpty()) {
                metadata.setContentType(contentType);
            }
            // 设置为inline，支持浏览器直接预览
            metadata.setContentDisposition("inline");

            PutObjectResult result = ossClient.putObject(
                    bucketName,
                    objectName,
                    new ByteArrayInputStream(bytes),
                    metadata
            );

            log.info("文件上传成功, ETag: {}, RequestId: {}", result.getETag(), result.getRequestId());
            return buildAccessUrl(objectName);

        } catch (OSSException oe) {
            log.error("OSS异常 - ErrorCode: {}, Message: {}, RequestId: {}",
                    oe.getErrorCode(), oe.getErrorMessage(), oe.getRequestId());
            throw new RuntimeException("文件上传失败: " + oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("客户端异常: {}", ce.getMessage());
            throw new RuntimeException("文件上传失败: " + ce.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     */
    public void delete(String objectName) {
        try {
            ossClient.deleteObject(bucketName, objectName);
            log.info("文件删除成功: {}", objectName);
        } catch (OSSException oe) {
            log.error("OSS异常 - ErrorCode: {}, Message: {}", oe.getErrorCode(), oe.getErrorMessage());
            throw new RuntimeException("文件删除失败: " + oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("客户端异常: {}", ce.getMessage());
            throw new RuntimeException("文件删除失败: " + ce.getMessage());
        }
    }

    /**
     * 生成带签名的访问URL（支持直接预览，无需自定义域名）
     * 预签名URL是一种安全链接，通过加密签名和有效期验证，临时授权访问特定的OSS文件
     * @param objectName        对象名称
     * @param expirationSeconds 过期时间（秒），最长7天
     * @return 签名URL
     */
    public String generatePresignedUrl(String objectName, int expirationSeconds) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + expirationSeconds * 1000L);
            URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
            return url.toString();
        } catch (Exception e) {
            log.error("生成签名URL失败: {}", e.getMessage());
            throw new RuntimeException("生成签名URL失败: " + e.getMessage());
        }
    }

    /**
     * 构建文件访问URL
     *
     * @param objectName 对象名称
     * @return 访问URL
     */
    private String buildAccessUrl(String objectName) {
        // 文件访问路径规则 https://BucketName.Endpoint/ObjectName
        return "https://" + bucketName + "." + endpoint + "/" + objectName;
    }

    /**
     * 获取Bucket名称
     *
     * @return bucket名称
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * 关闭客户端
     */
    public void shutdown() {
        if (ossClient != null) {
            ossClient.shutdown();
            log.info("OSS Client 已关闭");
        }
    }
}
