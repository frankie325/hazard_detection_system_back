package com.expressway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageDTO {
    @NotNull(message = "页码不能为空")
    private Integer current; //当前页码
    @NotNull(message = "每页条数不能为空")
    private Integer size; //每页条数
}
