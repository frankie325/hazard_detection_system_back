package com.expressway.service.impl;

import com.expressway.entity.SysArea;
import com.expressway.mapper.SysAreaMapper;
import com.expressway.service.SysAreaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAreaServicesImpl implements SysAreaService {
    @Resource
    private SysAreaMapper sysAreaMapper;

    /***
     * 查询区域列表
     */
    @Override
    public List<SysArea> getAllList() {
        return sysAreaMapper.selectAllArea();
    }
}
