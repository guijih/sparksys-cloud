package com.sparksys.commons.core.api.code;

/**
 * description: 封装API响应状态码
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:45:46
 */
public interface BaseExceptionCode {
    /**
     * code
     *
     * @return long
     * @author zhouxinlei
     * @date 2019-09-27 16:56:40
     */
    int getCode();

    /**
     * getMessage
     *
     * @return String
     * @author zhouxinlei
     * @date 2019-09-27 16:56:49
     */
    String getMessage();
}
