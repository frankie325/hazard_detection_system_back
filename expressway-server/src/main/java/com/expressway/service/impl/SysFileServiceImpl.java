package com.expressway.service.impl;

import com.expressway.context.BaseContext;
import com.expressway.entity.SysFile;
import com.expressway.mapper.SysFileMapper;
import com.expressway.service.SysFileService;
import com.expressway.vo.SysFileVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class SysFileServiceImpl implements SysFileService {

    @Resource
    private SysFileMapper sysFileMapper;
    
    // 文件上传根目录
    private static final String UPLOAD_BASE_PATH = "upload";
    
    // 允许的文件类型
    private static final String[] ALLOWED_TYPES = {
        "image/jpeg", "image/png", "image/gif", "image/webp",
        "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "text/plain", "text/csv"
    };
    
    // 最大文件大小：10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Override
    @Transactional
    public SysFileVO uploadFile(MultipartFile file) {
        // 验证文件是否为空
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (!isAllowedType(contentType)) {
            throw new IllegalArgumentException("不支持的文件类型");
        }
        
        // 获取当前用户ID
        Long uploadBy = BaseContext.getCurrentId();
        if (uploadBy == null) {
            throw new IllegalStateException("用户未登录");
        }
        
        // 创建上传目录
        String uploadDir = UPLOAD_BASE_PATH + File.separator + 
                          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;
        String filePath = uploadDir + File.separator + fileName;
        
        // 保存文件
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
        
        // 保存文件记录到数据库
        SysFile sysFile = new SysFile();
        sysFile.setFileName(originalFilename);
        sysFile.setFilePath(filePath.replace("\\", "/"));
        sysFile.setFileSize(file.getSize());
        sysFile.setFileType(contentType);
        sysFile.setUploadBy(uploadBy);
        sysFile.setCreateTime(LocalDateTime.now());
        
        int result = sysFileMapper.insertFile(sysFile);
        if (result != 1) {
            // 删除已上传的文件
            new File(filePath).delete();
            throw new RuntimeException("文件记录保存失败");
        }
        
        // 返回文件信息
        return sysFileMapper.selectFileVOById(sysFile.getId());
    }

    @Override
    public SysFileVO getFileById(Long id) {
        return sysFileMapper.selectFileVOById(id);
    }

    @Override
    public List<SysFileVO> getMyFiles() {
        Long uploadBy = BaseContext.getCurrentId();
        if (uploadBy == null) {
            throw new IllegalStateException("用户未登录");
        }
        return sysFileMapper.selectFilesByUploadBy(uploadBy);
    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        // 获取文件信息
        SysFile sysFile = sysFileMapper.selectFileById(id);
        if (sysFile == null) {
            throw new IllegalArgumentException("文件不存在");
        }
        
        // 验证文件所有者
        Long currentUserId = BaseContext.getCurrentId();
        if (!sysFile.getUploadBy().equals(currentUserId)) {
            throw new IllegalArgumentException("无权删除此文件");
        }
        
        // 删除文件
        File file = new File(sysFile.getFilePath());
        if (file.exists()) {
            file.delete();
        }
        
        // 删除数据库记录
        int result = sysFileMapper.deleteFileById(id);
        if (result != 1) {
            throw new RuntimeException("文件删除失败");
        }
    }
    
    /**
     * 验证文件类型是否允许
     */
    private boolean isAllowedType(String contentType) {
        for (String type : ALLOWED_TYPES) {
            if (type.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
}
