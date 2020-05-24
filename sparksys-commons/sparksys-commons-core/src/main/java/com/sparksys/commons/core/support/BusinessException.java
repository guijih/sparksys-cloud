package com.sparksys.commons.core.support;

/**
 * description: 业务异常类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:49:04
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = -7571216052553061849L;

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message) {
        super(message);
    }
}
