package com.expressway.vo;


import lombok.Data;

/**
 * 登出接口响应VO
 */
@Data
public class LogoutResponseVO {
    private Boolean success; // 是否成功
    private String message; // 提示信息
}