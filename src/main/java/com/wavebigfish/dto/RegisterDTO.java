package com.wavebigfish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Schema(description = "注册DTO")
@Data
public class RegisterDTO {

    @Schema(description = "用户名")
    private String userName;
    @Schema(description = "手机号")
    private String telephone;
    @Schema(description = "密码")
    private String password;
}