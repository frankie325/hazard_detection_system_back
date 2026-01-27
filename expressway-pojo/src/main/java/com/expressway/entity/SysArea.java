package com.expressway.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SysArea {
    private Long id;            // 区域id
    private Long parentId;      // 上级区域ID（0表示顶级区域）
    private String areaName;      // 区域名称
    private Long deptId;        // 管理部门id
    private Double length;      // 区域长度
    private Integer laneCount;  // 车道数
    private String coordinates; // 坐标信息
    private Date createTime;    // 创建时间
    private Date updateTime;    // 更新时间


}
