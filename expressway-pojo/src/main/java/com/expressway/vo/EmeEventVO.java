package com.expressway.vo;

import com.expressway.entity.EmeEvent;
import com.expressway.enumeration.AlarmLevel;
import com.expressway.enumeration.DetectEventType;
import com.expressway.enumeration.EmeEventStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmeEventVO extends EmeEvent {
    private String eventLevelName;      // 事件等级名称
    private String eventTypeName;      // 事件类型名称
    private String statusName;      // 状态名称

    // 获取时间等级中文名称
    public String getEventLevelName() {
        if (getEventLevel() != null) {
            return getEventLevel().getDescription();
        }
        return null;
    }

    // 获取事件类型中文名称
    public String getEventTypeName() {
        if (getEventType() != null) {
            return getEventType().getDescription();
        }
        return null;
    }

    // 获取状态中文名称
    public String getStatusName() {
        if (getStatus() != null) {
            return getStatus().getDescription();
        }
        return null;
    }
}
