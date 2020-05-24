package com.sparksys.commons.core.support;

/**
 * description: 授权认证异常类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:48:54
 */
public class AuthException extends Exception {

    private static final long serialVersionUID = -7571216052553061849L;

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String message) {
        super(message);
    }
}
