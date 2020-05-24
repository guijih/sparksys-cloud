package com.sparksys.commons.web.support;

import com.sparksys.commons.core.api.code.ResponseResultStatus;
import com.sparksys.commons.core.api.result.ApiResult;
import com.sparksys.commons.core.support.AuthException;
import com.sparksys.commons.core.support.BusinessException;
import com.sparksys.commons.core.support.BusinessValidationException;
import com.sparksys.commons.web.annotation.ResponseResult;
import com.sparksys.commons.web.constant.WebConstant;
import com.sparksys.commons.web.utils.HttpServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * description: 全局异常处理
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:44:48
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    public void handleResponseResult() {
        HttpServletRequest servletRequest = HttpServletUtils.getRequest();
        ResponseResult responseResult = (ResponseResult) servletRequest.getAttribute(WebConstant.RESPONSE_RESULT_ANN);
        boolean result = responseResult != null;
        if (result) {
            servletRequest.removeAttribute(WebConstant.RESPONSE_RESULT_ANN);
        }
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ApiResult businessValidationException(BusinessValidationException e) {
        handleResponseResult();
        log.error(e.getMessage());
        return ApiResult.apiResult(ResponseResultStatus.PARAM_VALID_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResult businessException(BusinessException e) {
        handleResponseResult();
        log.error(e.getMessage());
        return ApiResult.apiResult(ResponseResultStatus.FAILURE.getCode(), e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ApiResult businessAuthException(AuthException e) {
        handleResponseResult();
        log.error(e.getMessage());
        return ApiResult.apiResult(ResponseResultStatus.UN_AUTHORIZED.getCode(), e.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ApiResult accountNotFoundException(AccountNotFoundException e) {
        handleResponseResult();
        log.error(e.getMessage());
        return ApiResult.apiResult(ResponseResultStatus.UN_AUTHORIZED.getCode(), e.getMessage());
    }

    @ExceptionHandler(PasswordException.class)
    public ApiResult passwordException(PasswordException e) {
        handleResponseResult();
        log.error(e.getMessage());
        return ApiResult.apiResult(ResponseResultStatus.UN_AUTHORIZED.getCode(), e.getMessage());
    }

    /**
     * 405
     *
     * @param
     * @return ApiResult
     * @author zhouxinlei
     * @date 2019/5/25 0025
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResult httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        handleResponseResult();
        log.error(e.getMessage());
        return ApiResult.apiResult(ResponseResultStatus.METHOD_NOT_SUPPORTED);
    }

    /**
     * 404 没有找到访问资源
     *
     * @param
     * @return ApiResult
     * @author zhouxinlei
     * @date 2019/5/25 0025
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult notFoundPage404(NoHandlerFoundException e) {
        log.error(e.getMessage());
        return ApiResult.apiResult(ResponseResultStatus.NOT_FOUND);
    }

    /**
     * 415 不支持媒体类型
     *
     * @param e 异常
     * @return ApiResult
     * @author zhouxinlei
     * @date 2019/5/25 0025
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResult httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage());
        return ApiResult.apiResult(ResponseResultStatus.MEDIA_TYPE_NOT_SUPPORTED);
    }

    /**
     * 500 默认异常
     *
     * @param
     * @return ApiResult
     * @author zhouxinlei
     * @date 2019/5/25 0025
     */
    @ExceptionHandler(Exception.class)
    public ApiResult defaultException(Exception e) {
        handleResponseResult();
        log.error(e.getMessage());
        e.printStackTrace();
        return ApiResult.apiResult(ResponseResultStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 数据库异常
     *
     * @param e
     * @return ApiResult
     * @author zhouxinlei
     * @date 2019/5/25 0025
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public ApiResult handleException(SQLException e) {
        log.error("数据库异常{}", e.getMessage());
        return ApiResult.apiResult(e.getErrorCode(), "数据库异常");
    }

}
