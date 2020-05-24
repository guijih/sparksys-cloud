package com.sparksys.commons.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description: 登录认证用户实体类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:47:33
 */
@Data
@ApiModel(description = "登录用户实体类")
public class AuthUser implements Serializable {

    private static final long serialVersionUID = -6592610263703423919L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "账户")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "账户状态")
    private Integer status;

    @ApiModelProperty(value = "角色列表")
    private List<String> roles;

    @ApiModelProperty(value = "权限列表")
    private List<String> permissions;

}
