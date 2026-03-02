package com.expressway.service.impl;

import com.expressway.dto.EmeEventResourceBindDTO;
import com.expressway.entity.EmeEventResource;
import com.expressway.mapper.EmeEventResourceMapper;
import com.expressway.mapper.EmeResourceMapper;
import com.expressway.service.EmeResourceService;
import com.expressway.vo.EmeResourceVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmeResourceServiceImpl implements EmeResourceService {

    @Resource
    private EmeResourceMapper emeResourceMapper;

    @Resource
    private EmeEventResourceMapper emeEventResourceMapper;

    @Override
    public List<EmeResourceVO> getEmeResourceList() {
        return emeResourceMapper.selectAllEmeResource();
    }

    @Override
    @Transactional
    public void bindEventResource(EmeEventResourceBindDTO bindDTO) {
        Long eventId = bindDTO.getEventId();
        List<Long> resourceIds = bindDTO.getResourceIds();

        // 构建关联列表
        List<EmeEventResource> eventResources = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            EmeEventResource eventResource = new EmeEventResource();
            eventResource.setEventId(eventId);
            eventResource.setResourceId(resourceId);
            eventResources.add(eventResource);
        }

        // 批量插入关联关系
        emeEventResourceMapper.batchInsert(eventResources);
    }
}
