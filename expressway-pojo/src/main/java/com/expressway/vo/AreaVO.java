package com.expressway.vo;

import com.expressway.entity.SysArea;
import lombok.Data;

@Data
public class AreaVO extends SysArea {
    private String deptName;
    private int deviceCount;
}
