package com.expressway.service;

import com.expressway.vo.SysFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysFileService {
    /**
     * 上传文件
     */
    SysFileVO uploadFile(MultipartFile file);
    
    /**
     * 根据ID获取文件详情
     */
    SysFileVO getFileById(Long id);
    
    /**
     * 获取当前用户上传的文件列表
     */
    List<SysFileVO> getMyFiles();
    
    /**
     * 删除文件
     */
    void deleteFile(Long id);
}
