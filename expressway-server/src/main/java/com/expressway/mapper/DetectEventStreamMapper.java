package com.expressway.mapper;

import com.expressway.dto.DetectEventStreamQueryParamsDTO;
import com.expressway.vo.DetectEventStreamVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DetectEventStreamMapper {
    /**
     * 查询事件流列表（关联区域和设备）
     */
    List<DetectEventStreamVO> selectEventStreamList(DetectEventStreamQueryParamsDTO queryParams);
}
