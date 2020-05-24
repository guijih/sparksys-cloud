package com.sparksys.commons.security.entity;

import com.sparksys.commons.core.entity.AuthUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: token实体类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:38:01
 */
@Data
@ApiModel(value = "登录返回", description = "登录返回")
public class AuthToken implements Serializable {

    private static final long serialVersionUID = 6385548035127824037L;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "有效期")
    private Long expiration;

    @ApiModelProperty(value = "登录用户信息")
    private AuthUser authUser;


}
