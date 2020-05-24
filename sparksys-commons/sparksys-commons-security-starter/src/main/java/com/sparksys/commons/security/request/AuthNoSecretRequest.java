package com.sparksys.commons.security.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 免密登录请求参数
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:38:43
 */
@Data
@ApiModel(description = "登录请求参数")
public class AuthNoSecretRequest {

    @ApiModelProperty(value = "账户")
    private String account;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

}
