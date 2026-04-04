package com.expressway.mapper;

import com.expressway.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文件信息Mapper
 */
@Mapper
public interface SysFileMapper {
    /**
     * 插入文件记录
     */
    int insert(SysFile sysFile);

    /**
     * 根据ID查询
     */
    SysFile selectById(Long id);

    /**
     * 根据业务类型和业务ID查询文件列表
     */
    List<SysFile> selectByBusiness(String businessType, Long businessId);

    /**
     * 根据业务类型和业务ID删除文件记录
     */
    int deleteByBusiness(String businessType, Long businessId);

    /**
     * 根据ID删除
     */
    int deleteById(Long id);

    /**
     * 根据上传用户ID查询文件列表
     */
    List<SysFile> selectByUserId(Long uploadUserId);

    /**
     * 查询所有文件
     */
    List<SysFile> selectAll();
}
