package com.sparksys.commons.security.controller;


import com.sparksys.commons.core.entity.AuthUser;
import com.sparksys.commons.security.entity.AuthToken;
import com.sparksys.commons.security.request.AuthNoSecretRequest;
import com.sparksys.commons.security.request.AuthRequest;
import com.sparksys.commons.security.service.AuthSecurityRequest;
import com.sparksys.commons.web.annotation.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * description: 授权认证controller
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:37:27
 */
@Slf4j
@RestController
@RequestMapping("/security/oauth")
@ResponseResult
@Api(tags = "登录管理")
public class AuthUserController {

    private final AuthSecurityRequest authSecurityRequest;

    @Autowired
    public AuthUserController(AuthSecurityRequest authSecurityRequest) {
        this.authSecurityRequest = authSecurityRequest;
    }

    @ApiOperation("系统登录")
    @PostMapping("/login")
    public AuthToken login(@RequestBody AuthRequest authRequest) throws Exception {
        return authSecurityRequest.getAccessToken(authRequest);
    }

    @ApiOperation("免密登录")
    @PostMapping("/noSecretLogin")
    public AuthToken noSecretLogin(@RequestBody AuthNoSecretRequest authNoSecretRequest) throws Exception {
        return authSecurityRequest.noSecretAccessToken(authNoSecretRequest);
    }

    @ApiOperation("获取登录用户信息")
    @GetMapping("/getOauthInfo")
    public AuthUser getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        return authSecurityRequest.getUserInfo(httpServletRequest);
    }
}
