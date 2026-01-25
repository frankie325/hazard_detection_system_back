package com.expressway.mapper;
import com.expressway.entity.SysArea;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAreaMapper {
    /***
     * 查询所有区域
     */
    List<SysArea> selectAllArea();
}
