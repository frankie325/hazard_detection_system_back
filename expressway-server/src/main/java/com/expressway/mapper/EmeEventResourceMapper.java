package com.expressway.mapper;

import com.expressway.entity.EmeEventResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 应急事件资源关联Mapper
 */
@Mapper
public interface EmeEventResourceMapper {
    /**
     * 批量插入应急事件资源关联
     */
    void batchInsert(List<EmeEventResource> eventResources);

    /**
     * 根据事件ID删除关联
     */
    void deleteByEventId(Long eventId);

    /**
     * 根据事件ID查询关联列表
     */
    List<EmeEventResource> selectByEventId(Long eventId);
}
