package com.expressway.dto;

import com.expressway.enumeration.ActionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 应急事件状态更新DTO
 */
@Data
public class EmeEventStatusUpdateDTO {
    @NotNull(message = "事件ID不能为空")
    private Long eventId;           // 事件ID

    @NotNull(message = "操作类型不能为空")
    private ActionType actionType;  // 操作类型

    private Long receiverRoleId;    // 接收角色ID（资源绑定时需要）

    private List<Long> resourceIds; // 资源ID列表（资源绑定时需要）

    private String departure; // 出发地（资源绑定时需要）
    private String destination; //目的地（资源绑定时需要）
    private String remark;
}
