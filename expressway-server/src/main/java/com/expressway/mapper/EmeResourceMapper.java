package com.expressway.mapper;

import com.expressway.vo.EmeResourceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmeResourceMapper {
    /**
     * 查询所有资源池列表
     */
    List<EmeResourceVO> selectAllEmeResource();
}
