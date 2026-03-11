package com.expressway.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysFile {
    /**
     * 文件ID
     */
    private Long id;
    
    /**
     * 文件名称
     */
    private String fileName;
    
    /**
     * 文件存储路径
     */
    private String filePath;
    
    /**
     * 文件大小(字节)
     */
    private Long fileSize;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 上传人ID
     */
    private Long uploadBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
