package com.sparksys.commons.security.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 登录请求参数
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:38:55
 */
@Data
@ApiModel(description = "登录请求参数")
public class AuthRequest {

    @ApiModelProperty(value = "账户")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

}
