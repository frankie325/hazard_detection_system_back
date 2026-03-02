package com.expressway.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 应急事件资源绑定DTO
 */
@Data
public class EmeEventResourceBindDTO {
    @NotNull(message = "应急事件ID不能为空")
    private Long eventId;          // 应急事件ID

    @NotEmpty(message = "资源ID列表不能为空")
    private List<Long> resourceIds; // 资源ID列表


}
