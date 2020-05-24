package com.sparksys.commons.security.utils;


import com.sparksys.commons.core.api.code.ResponseResultStatus;
import com.sparksys.commons.core.api.result.ApiResult;
import com.sparksys.commons.core.constant.CoreConstant;
import com.sparksys.commons.web.component.SpringContextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description: SecurityResponse工具类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:40:03
 */
public class SecurityResponse {

    public static String getAuthHeader(HttpServletRequest httpRequest) {
        String header = httpRequest.getHeader(CoreConstant.JwtTokenConstant.JWT_TOKEN_HEADER);
        return StringUtils.removeStart(header, CoreConstant.JwtTokenConstant.JWT_TOKEN_HEAD);
    }

    public static void unauthorized(HttpServletResponse response) {
        ObjectMapper objectMapper = SpringContextUtils.getBean(ObjectMapper.class);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(objectMapper.writeValueAsString(ApiResult.apiResult(ResponseResultStatus.UN_AUTHORIZED)));
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void forbidden(HttpServletResponse response) {
        ObjectMapper objectMapper = SpringContextUtils.getBean(ObjectMapper.class);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(objectMapper.writeValueAsString(ApiResult.apiResult(ResponseResultStatus.REQ_REJECT)));
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
