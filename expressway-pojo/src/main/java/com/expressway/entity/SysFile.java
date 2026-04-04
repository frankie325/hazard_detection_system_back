package com.expressway.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件信息实体
 */
@Data
public class SysFile {
    private Long id;                    // 文件ID
    private String fileName;            // 原始文件名
    private String filePath;            // OSS访问URL
    private Long fileSize;              // 文件大小(字节)
    private String fileType;            // 文件类型
    private String bucketName;          // OSS bucket名称
    private String objectKey;           // OSS对象key
    private String businessType;        // 业务类型
    private Long businessId;            // 关联业务ID
    private Long uploadUserId;          // 上传用户ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;   // 创建时间
}
