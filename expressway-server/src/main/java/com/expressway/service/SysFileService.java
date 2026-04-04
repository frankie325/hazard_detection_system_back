package com.expressway.service;

import com.expressway.entity.SysFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务接口
 */
public interface SysFileService {
    /**
     * 上传文件
     *
     * @param file         文件
     * @return 文件信息
     */
    SysFile upload(MultipartFile file);

    /**
     * 批量上传文件
     *
     * @param files        文件数组
     * @return 文件信息列表
     */
    List<SysFile> uploadBatch(MultipartFile[] files);

    /**
     * 根据ID查询文件
     */
    SysFile getById(Long id);

    /**
     * 删除文件（同时删除OSS文件和数据库记录）
     */
    void deleteById(Long id);

    /**
     * 获取当前用户上传的文件列表
     */
    List<SysFile> getFiles();

    /**
     * 获取所有文件列表
     */
    List<SysFile> getAllFiles();
}
