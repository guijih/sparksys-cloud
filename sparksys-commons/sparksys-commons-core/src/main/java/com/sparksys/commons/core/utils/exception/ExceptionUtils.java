package com.sparksys.commons.core.utils.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * description: 异常处理工具类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:53:29
 */
public class ExceptionUtils {

    /**
     * 将CheckedException转换为UncheckedException
     *
     * @param e
     * @return RuntimeException
     * @throws
     * @Author zhouxinlei
     * @Date 2019-12-27 10:10:56
     */
    public static RuntimeException unchecked(Throwable e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    /**
     * 代理异常解包
     *
     * @param wrapped 包装过得异常
     * @return Throwable 解包后的异常
     * @throws
     * @Author zhouxinlei
     * @Date 2019-12-27 10:10:33
     */
    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

}
