package com.expressway.mapper;

import com.expressway.entity.SysFile;
import com.expressway.vo.SysFileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysFileMapper {
    /**
     * 插入文件记录
     */
    int insertFile(SysFile sysFile);
    
    /**
     * 根据ID查询文件
     */
    SysFile selectFileById(Long id);
    
    /**
     * 根据ID查询文件详情
     */
    SysFileVO selectFileVOById(Long id);
    
    /**
     * 根据上传人查询文件列表
     */
    List<SysFileVO> selectFilesByUploadBy(Long uploadBy);
    
    /**
     * 根据ID删除文件
     */
    int deleteFileById(Long id);
}
