package com.expressway.service.impl;

import com.expressway.context.BaseContext;
import com.expressway.dto.EmeEventAddDTO;
import com.expressway.dto.EmeEventResourceBindDTO;
import com.expressway.dto.EmeEventStatusUpdateDTO;
import com.expressway.dto.EmeTimelineAddDTO;
import com.expressway.entity.EmeEvent;
import com.expressway.entity.EmeTimeline;
import com.expressway.entity.SysRole;
import com.expressway.entity.SysUser;
import com.expressway.enumeration.ActionType;
import com.expressway.enumeration.EmeEventStatus;
import com.expressway.exception.EmeEventException;
import com.expressway.mapper.EmeEventMapper;
import com.expressway.mapper.EmeTimelineMapper;
import com.expressway.mapper.SysRoleMapper;
import com.expressway.mapper.SysUserMapper;
import com.expressway.service.EmeEventService;
import com.expressway.service.EmeResourceService;
import com.expressway.vo.EmeEventVO;
import com.expressway.vo.EmeTimelineVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmeEventServiceImpl implements EmeEventService {

    @Resource
    private EmeEventMapper emeEventMapper;
    @Resource
    private EmeTimelineMapper emeTimelineMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private EmeResourceService emeResourceService;

    @Override
    public List<EmeEventVO> getEmeEventList() {
        // 获取当前登录用户ID
        Long currentUserId = BaseContext.getCurrentId();
        if (currentUserId == null) {
            throw new EmeEventException("用户未登录");
        }

        // 查询当前用户信息，获取角色ID
        SysUser currentUser = sysUserMapper.selectById(currentUserId);
        if (currentUser == null) {
            throw new EmeEventException("用户信息不存在");
        }

        // 查询角色信息，获取部门ID
        SysRole role = sysRoleMapper.selectById(currentUser.getRoleId());
        if (role == null) {
            throw new EmeEventException("用户角色信息不存在");
        }

        // 根据部门ID过滤查询应急事件: 当前登录用户为处置部门下的成员才可以看到该事件
        return emeEventMapper.selectEmeEventByDeptId(role.getDeptId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createEmeEvent(EmeEventAddDTO addDTO) {
        EmeEvent emeEvent = new EmeEvent();
        BeanUtils.copyProperties(addDTO, emeEvent);
        // 默认状态为启动
        emeEvent.setStatus(EmeEventStatus.START);
        int result = emeEventMapper.insertEmeEvent(emeEvent);
        if (result != 1) {
            throw new EmeEventException("新增事件失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTimeline(EmeTimelineAddDTO timeline) {
        // 参数校验
        if (timeline == null || timeline.getEventId() == null) {
            throw new EmeEventException("时间线参数无效");
        }

        // 获取当前登录用户ID作为执行者
        Long currentUserId = BaseContext.getCurrentId();
        if (currentUserId == null) {
            throw new EmeEventException("用户未登录");
        }

        EmeTimeline emeTimeline = new EmeTimeline();
        BeanUtils.copyProperties(timeline, emeTimeline);
        // 设置执行者ID为当前登录用户
        emeTimeline.setOperatorId(currentUserId);
        // 如果没有传入操作时间，使用当前时间
        if (emeTimeline.getOperateTime() == null) {
            emeTimeline.setOperateTime(LocalDateTime.now());
        }

        int result = emeTimelineMapper.insertEmeTimeline(emeTimeline);
        if (result != 1) {
            throw new EmeEventException("生成时间线失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEventStatus(EmeEventStatusUpdateDTO updateDTO) {
        Long currentUserId = BaseContext.getCurrentId();
        if (currentUserId == null) {
            throw new EmeEventException("用户未登录");
        }

        // 查询事件
        EmeEvent event = emeEventMapper.selectById(updateDTO.getEventId());
        if (event == null) {
            throw new EmeEventException("事件不存在");
        }

        ActionType actionType = updateDTO.getActionType();
        EmeEventStatus newStatus = null;
        String actionText = "";

        switch (actionType) {
            case CONFIRM:
                // 确认事件：状态 START -> CONFIRMED
                if (event.getStatus() != EmeEventStatus.START) {
                    throw new EmeEventException("当前状态不允许确认操作");
                }
                newStatus = EmeEventStatus.CONFIRMED;
                actionText = "事件已确认";
                break;

            case BIND:
                // 资源绑定：需要指定接收角色，状态 CONFIRMED -> DISPATCHING
                if (event.getStatus() != EmeEventStatus.CONFIRMED) {
                    throw new EmeEventException("当前状态不允许资源绑定操作");
                }
                if (updateDTO.getReceiverRoleId() == null) {
                    throw new EmeEventException("请指定接收角色");
                }
                if (updateDTO.getResourceIds() == null || updateDTO.getResourceIds().isEmpty()) {
                    throw new EmeEventException("请选择要绑定的资源");
                }
                // 设置接收角色
                event.setReceiverRoleId(updateDTO.getReceiverRoleId());
                // 调用资源绑定接口
                EmeEventResourceBindDTO bindDTO = new EmeEventResourceBindDTO();
                bindDTO.setEventId(updateDTO.getEventId());
                bindDTO.setResourceIds(updateDTO.getResourceIds());
                emeResourceService.bindEventResource(bindDTO);
                newStatus = EmeEventStatus.DISPATCHING;
                actionText = "资源已绑定，已指派接收角色";
                break;

            case SIGN:
                // 到场签到：只有接收角色下的用户可以操作，状态 DISPATCHING -> PROCESSING
                if (event.getStatus() != EmeEventStatus.DISPATCHING) {
                    throw new EmeEventException("当前状态不允许签到操作");
                }
                checkReceiverRole(event.getReceiverRoleId(), currentUserId, "签到");
                newStatus = EmeEventStatus.PROCESSING;
                actionText = "已到场签到，开始处理";
                break;

            case CLOSED:
                // 关闭：只有接收角色下的用户可以操作，状态 PROCESSING -> CLOSED
//                if (event.getStatus() != EmeEventStatus.PROCESSING) {
//                    throw new EmeEventException("当前状态不允许关闭操作");
//                }
                //checkReceiverRole(event.getReceiverRoleId(), currentUserId, "关闭");
                newStatus = EmeEventStatus.CLOSED;
                actionText = "事件已关闭";
                break;
            case REMARK:
                if (event.getStatus() == EmeEventStatus.CLOSED) {
                    throw new EmeEventException("当前状态不允许备注操作");
                }
                break;
            case ATTACHMENT:
                if (event.getStatus() == EmeEventStatus.CLOSED) {
                    throw new EmeEventException("当前状态不允许上传附件操作");
                }
                break;
            default:
                throw new EmeEventException("不支持的操作类型");
        }

        // 更新事件状态
        event.setStatus(newStatus);
        emeEventMapper.updateEmeEvent(event);

        // 创建时间线
        EmeTimelineAddDTO timelineDTO = new EmeTimelineAddDTO();
        timelineDTO.setEventId(updateDTO.getEventId());
        timelineDTO.setActionType(actionType);
        timelineDTO.setActionText(actionText);
        timelineDTO.setDeparture(updateDTO.getDeparture());
        timelineDTO.setDestination(updateDTO.getDestination());
        timelineDTO.setRemark(updateDTO.getRemark());
        createTimeline(timelineDTO);
    }

    @Override
    public List<EmeTimelineVO> getTimelineByEventId(Long eventId) {
        if (eventId == null) {
            throw new EmeEventException("事件ID不能为空");
        }
        return emeTimelineMapper.selectByEmergencyId(eventId);
    }

    @Override
    public EmeEventVO getEventById(Long id) {
        if (id == null) {
            throw new EmeEventException("事件ID不能为空");
        }
        EmeEventVO eventVO = emeEventMapper.selectVOById(id);
        if (eventVO == null) {
            throw new EmeEventException("事件不存在");
        }
        return eventVO;
    }

    /**
     * 检查当前用户是否为接收角色下的用户
     */
    private void checkReceiverRole(Long receiverRoleId, Long currentUserId, String operation) {
        SysUser currentUser = sysUserMapper.selectById(currentUserId);
        if (currentUser == null || !receiverRoleId.equals(currentUser.getRoleId())) {
            throw new EmeEventException("只有接收角色下的用户才能进行" + operation + "操作");
        }
    }
}
