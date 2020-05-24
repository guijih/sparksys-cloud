package com.sparksys.commons.web.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description: HttpServlet工具类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:45:00
 */
public class HttpServletUtils {

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }


    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        assert servletRequestAttributes != null;
        return servletRequestAttributes;
    }
}
