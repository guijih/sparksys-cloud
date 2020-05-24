package com.sparksys.commons.web.service;

import com.sparksys.commons.core.constant.AuthConstant;
import com.sparksys.commons.core.entity.AuthUser;
import com.sparksys.commons.core.support.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * description: 请求用户接口抽象类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:44:10
 */
@Slf4j
public abstract class AbstractAuthSecurityRequest {

    /**
     * 根据token获取认证用户
     *
     * @param accessToken
     * @return AuthUser
     * @throws
     * @Author zhouxinlei
     * @Date 2020-01-03 15:47:42
     */
    public AuthUser getUserInfo(String accessToken) throws AuthException {
        log.info("accessToken is {}", accessToken);
        String cacheKey = AuthConstant.AUTH_USER.concat(accessToken);
        AuthUser authUser = getCache(cacheKey);
        if (ObjectUtils.isEmpty(authUser)) {
            throw new AuthException("暂未登录或token已过期");
        }
        return authUser;
    }

    /**
     * 缓存中获取用户信息
     *
     * @param key
     * @return AuthUser
     * @throws
     * @Author zhouxinlei
     * @Date 2020-05-24 10:09:32
     */
    protected abstract AuthUser getCache(String key);

}
