package com.expressway.dto;

import com.expressway.enumeration.Sex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增用户DTO
 */
@Data
public class UserAddDTO {
    @NotBlank(message = "姓名不能为空")
    private String name;       // 姓名
    @NotNull(message = "性别不能为空")
    private Sex gender;     // 性别（M-男，F-女）

    @NotBlank(message = "用户名不能为空")
    private String username;   // 登录用户名

    @NotBlank(message = "密码不能为空")
    private String password;   // 密码
    @NotBlank(message = "手机号不能为空")
    private String phone;      // 手机号
    @NotBlank(message = "身份证号不能为空")
    private String idCard;     // 身份证号

    @NotNull(message = "所属角色不能为空")
    private Long roleId;       // 所属角色ID

    private String remark;     // 备注
}
