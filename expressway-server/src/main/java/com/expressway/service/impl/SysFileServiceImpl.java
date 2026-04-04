package com.expressway.service.impl;

import com.expressway.context.BaseContext;
import com.expressway.entity.SysFile;
import com.expressway.exception.FileException;
import com.expressway.mapper.SysFileMapper;
import com.expressway.service.SysFileService;
import com.expressway.utils.AliOssUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件服务实现类
 */
@Service
public class SysFileServiceImpl implements SysFileService {

    @Resource
    private AliOssUtil aliOssUtil;

    @Resource
    private SysFileMapper sysFileMapper;

    @Override
    public SysFile upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileException("上传文件不能为空");
        }

        try {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            // 获取文件扩展名
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            // 生成唯一文件名
            String objectKey = UUID.randomUUID().toString() + extension;
            
            // 上传到OSS
            String filePath = aliOssUtil.upload(file.getBytes(), objectKey, file.getContentType());
            
            // 获取文件类型
            String fileType = extension.replace(".", "").toLowerCase();
            
            // 保存文件信息到数据库
            SysFile sysFile = new SysFile();
            sysFile.setFileName(originalFilename);
            sysFile.setFilePath(filePath);  // 数据库存普通URL
            sysFile.setFileSize(file.getSize());
            sysFile.setFileType(fileType);
            sysFile.setBucketName(aliOssUtil.getBucketName());
            sysFile.setObjectKey(objectKey);
            sysFile.setUploadUserId(BaseContext.getCurrentId());
            
            sysFileMapper.insert(sysFile);
            
            // 返回时使用签名URL，支持预览
            sysFile.setFilePath(aliOssUtil.generatePresignedUrl(objectKey, 7 * 24 * 3600));
            
            return sysFile;
        } catch (IOException e) {
            throw new FileException("文件上传失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<SysFile> uploadBatch(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new FileException("上传文件不能为空");
        }

        List<SysFile> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            SysFile sysFile = upload(file);
            uploadedFiles.add(sysFile);
        }
        return uploadedFiles;
    }

    @Override
    public SysFile getById(Long id) {
        if (id == null) {
            throw new FileException("文件ID不能为空");
        }
        SysFile sysFile = sysFileMapper.selectById(id);
        if (sysFile == null) {
            throw new FileException("文件不存在");
        }
        // 动态生成签名URL，支持预览（7天有效）
        sysFile.setFilePath(aliOssUtil.generatePresignedUrl(sysFile.getObjectKey(), 7 * 24 * 3600));
        return sysFile;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        SysFile sysFile = getById(id);
        
        // 删除OSS文件（需要扩展AliOssUtil添加删除方法）
        // aliOssUtil.delete(sysFile.getObjectKey());
        
        // 删除数据库记录
        sysFileMapper.deleteById(id);
    }

    @Override
    public List<SysFile> getFiles() {
        Long currentUserId = BaseContext.getCurrentId();
        if (currentUserId == null) {
            throw new FileException("用户未登录");
        }
        List<SysFile> files = sysFileMapper.selectByUserId(currentUserId);
        // 为每个文件生成签名URL
        files.forEach(file -> file.setFilePath(
            aliOssUtil.generatePresignedUrl(file.getObjectKey(), 7 * 24 * 3600)
        ));
        return files;
    }

    @Override
    public List<SysFile> getAllFiles() {
        List<SysFile> files = sysFileMapper.selectAll();
        // 为每个文件生成签名URL
//        files.forEach(file -> file.setFilePath(
//            aliOssUtil.generatePresignedUrl(file.getObjectKey(), 7 * 24 * 3600)
//        ));
        return files;
    }
}
