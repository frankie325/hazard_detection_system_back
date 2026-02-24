package com.expressway.service.impl;

import com.expressway.dto.EmeEventAddDTO;
import com.expressway.entity.EmeEvent;
import com.expressway.enumeration.EmeEventStatus;
import com.expressway.exception.EmeEventException;
import com.expressway.mapper.EmeEventMapper;
import com.expressway.service.EmeEventService;
import com.expressway.vo.EmeEventVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmeEventServiceImpl implements EmeEventService {

    @Resource
    private EmeEventMapper emeEventMapper;

    @Override
    public List<EmeEventVO> getEmeEventList() {
        return emeEventMapper.selectAllEmeEvent();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createEmeEvent(EmeEventAddDTO addDTO) {
        EmeEvent emeEvent = new EmeEvent();
        BeanUtils.copyProperties(addDTO, emeEvent);
        // 默认状态为已确认
        emeEvent.setStatus(EmeEventStatus.CONFIRMED);
        int result = emeEventMapper.insertEmeEvent(emeEvent);
        if (result != 1) {
            throw new EmeEventException("新增事件失败");
        }
    }
}
