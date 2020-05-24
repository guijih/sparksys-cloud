package com.sparksys.commons.web.component;

import com.sparksys.commons.core.constant.CoreConstant;
import com.sparksys.commons.core.entity.AuthUser;
import com.sparksys.commons.web.service.AbstractAuthSecurityRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * description: 全局获取用户信息
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:41:46
 */
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AbstractAuthSecurityRequest abstractAuthSecurityRequest;

    public AuthUserArgumentResolver(AbstractAuthSecurityRequest abstractAuthSecurityRequest) {
        this.abstractAuthSecurityRequest = abstractAuthSecurityRequest;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == AuthUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest
            , WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        assert servletRequest != null;
        String header = servletRequest.getHeader(CoreConstant.JwtTokenConstant.JWT_TOKEN_HEADER);
        String accessToken = StringUtils.removeStart(header, CoreConstant.JwtTokenConstant.JWT_TOKEN_HEAD);
        return abstractAuthSecurityRequest.getUserInfo(accessToken);
    }
}
