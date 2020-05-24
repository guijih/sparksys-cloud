package com.sparksys.authorization.web;


import cn.hutool.core.util.StrUtil;
import com.sparksys.commons.core.constant.CoreConstant;
import com.sparksys.commons.web.annotation.ResponseResult;
import com.sparksys.commons.web.utils.HttpServletUtils;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 用户 前端控制器
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:25:32
 */

@RestController
@ResponseResult
@RequestMapping("/user")
@Api(tags = "用户管理")
public class AuthUserController {

    @GetMapping("/getCurrentUser")
    @ApiOperation("获取当前用户")
    public Object getCurrentUser() {
        String header = HttpServletUtils.getRequest().getHeader("Authorization");
        String token = StrUtil.subAfter(header, CoreConstant.JwtTokenConstant.JWT_TOKEN_HEAD, false);
        return Jwts.parser()
                .setSigningKey("sparksys".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

}
