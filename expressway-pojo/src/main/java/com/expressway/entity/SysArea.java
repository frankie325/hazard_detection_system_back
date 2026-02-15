package com.expressway.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class SysArea {
    private Long id;            // 区域id
    private String areaName;      // 区域名称
    private Long deptId;        // 管理部门id
    private Double length;      // 区域长度
    private Integer laneCount;  // 车道数
    private String coordinates; // 坐标信息
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;    // 更新时间


}
