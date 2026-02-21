package com.expressway.service.impl;

import com.expressway.dto.DetectEventStreamQueryParamsDTO;
import com.expressway.mapper.DetectEventStreamMapper;
import com.expressway.service.DetectEventStreamService;
import com.expressway.vo.DetectEventStreamVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetectEventStreamServiceImpl implements DetectEventStreamService {

    @Resource
    private DetectEventStreamMapper detectEventStreamMapper;

    @Override
    public PageInfo<DetectEventStreamVO> getEventStreamList(DetectEventStreamQueryParamsDTO queryParams) {
        PageHelper.startPage(queryParams.getCurrent(), queryParams.getSize());
        List<DetectEventStreamVO> list = detectEventStreamMapper.selectEventStreamList(queryParams);
        return new PageInfo<>(list);
    }
}
