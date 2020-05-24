package com.sparksys.commons.web.aspect;

import com.sparksys.commons.web.utils.HttpServletUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * description: web请求日志切面
 *
 * @Author zhouxinlei
 * @Date  2020-05-24 13:41:01
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String START_TIME = "request-start";

    @Pointcut("execution(* com.sparksys.*.web..*Controller.*(..))")
    public void pointCut() {
    }

    /**
     * 前置通知
     *
     * @param joinPoint 切入点
     * @return void
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws JsonProcessingException {
        HttpServletRequest request = HttpServletUtils.getRequest();
        StringBuilder stringBuilder = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        if (args != null || args.length > 0) {
            for (Object object : args) {
                if (object != null) {
                    if (object instanceof ServletRequest
                            || object instanceof ServletResponse
                            || object instanceof MultipartFile) {
                        continue;
                    }
                    stringBuilder
                            .append(objectMapper.writeValueAsString(object))
                            .append("\n").append(",");
                }
            }
        }
        if (!"".contentEquals(stringBuilder)) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        String method = joinPoint.getTarget().getClass().getName().concat(".").concat(joinPoint.getSignature().getName());
        log.info("【请求URL】：{}", request.getRequestURL());
        log.info("【请求IP】：{}", getIpAddress(request));
        log.info("【请求类名】：{}，【请求方法名】：{}", request.getMethod(), method);
        log.info("【请求参数】：【{}】", stringBuilder.toString());
        Long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);
    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("pointCut()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        log.info("【返回值】：【{}】", objectMapper.writeValueAsString(result));
        return result;
    }

    /**
     * 后置通知
     *
     * @param joinPoint 切入点
     * @param keys
     * @return void
     */
    @AfterReturning(pointcut = "pointCut()", returning = "keys")
    public void after(JoinPoint joinPoint, Object keys) throws JsonProcessingException {
        String method = joinPoint.getTarget().getClass().getName().concat(".").concat(joinPoint.getSignature().getName());
        log.info("【执行方法】：【{}】", method);
        log.info("【返回结果】：【{}】", objectMapper.writeValueAsString(keys));
    }

    /**
     * 异常通知，拦截记录异常日志
     *
     * @return void
     */
    @AfterThrowing(pointcut = "pointCut()")
    public void afterThrowing() {
        HttpServletRequest request = HttpServletUtils.getRequest();
        Long start = (Long) request.getAttribute(START_TIME);
        Long end = System.currentTimeMillis();
        log.info("【接口请求耗时】：{}毫秒", end - start);
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址。
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串
     *
     * @param request
     * @return String
     */
    private static String getIpAddress(HttpServletRequest request) {
        String ignoreCase = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ignoreCase.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ignoreCase.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ignoreCase.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ignoreCase.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ignoreCase.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            String localIp = "127.0.0.1";
            String ignoreIp = "0:0:0:0:0:0:0:1";
            if (localIp.equals(ip) || ignoreIp.equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                assert inetAddress != null;
                ip = inetAddress.getHostAddress();
            }
        }
        return ip;
    }
}
