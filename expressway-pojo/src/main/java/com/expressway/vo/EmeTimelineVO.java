package com.expressway.vo;

import com.expressway.entity.EmeTimeline;
import com.expressway.enumeration.ActionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmeTimelineVO extends EmeTimeline {
    private String operatorName;        // 执行者姓名
    private ActionType actionType;      // 操作类型
    private String actionTypeName;      // 操作类型名称

    public String getActionTypeName() {
        if (getActionType() != null) {
            return getActionType().getDescription();
        }
        return null;
    }

}
