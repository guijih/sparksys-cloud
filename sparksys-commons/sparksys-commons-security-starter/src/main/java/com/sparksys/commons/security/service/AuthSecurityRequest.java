package com.sparksys.commons.security.service;

import com.sparksys.commons.core.constant.AuthConstant;
import com.sparksys.commons.core.constant.CoreConstant;
import com.sparksys.commons.core.entity.AuthUser;
import com.sparksys.commons.redis.cache.CacheProviderService;
import com.sparksys.commons.security.entity.AuthToken;
import com.sparksys.commons.security.request.AuthNoSecretRequest;
import com.sparksys.commons.security.request.AuthRequest;
import com.sparksys.commons.web.service.AbstractAuthSecurityRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * projectName：parent
 * packageName：com.primus.mall.security.service
 * description：
 *
 * @Author zhoux
 * @Date 2020/1/3 0003 11:07
 */
@Service
@Slf4j
public class AuthSecurityRequest extends AbstractAuthSecurityRequest {

    private final CacheProviderService cacheProviderService;
    private AbstractAuthDetailsService authSecurityDetailsService;

    public AuthSecurityRequest(@Qualifier("redisCacheProvider") CacheProviderService cacheProviderService,
                               AbstractAuthDetailsService authSecurityDetailsService) {
        this.cacheProviderService = cacheProviderService;
        this.authSecurityDetailsService = authSecurityDetailsService;
    }

    public AuthToken getAccessToken(AuthRequest authRequest) throws Exception {
        return authSecurityDetailsService.login(authRequest);
    }

    public AuthUser getUserInfo(HttpServletRequest servletRequest) throws Exception {
        String header = servletRequest.getHeader(CoreConstant.JwtTokenConstant.JWT_TOKEN_HEADER);
        String accessToken = StringUtils.removeStart(header, CoreConstant.JwtTokenConstant.JWT_TOKEN_HEAD);
        log.info("accessToken is {}", accessToken);
        return getUserInfo(accessToken);
    }

    @Override
    protected AuthUser getCache(String key) {
        return cacheProviderService.get(AuthConstant.AUTH_USER + key);
    }

    public AuthToken noSecretAccessToken(AuthNoSecretRequest authNoSecretRequest) throws Exception {
        return authSecurityDetailsService.noSecretLogin(authNoSecretRequest);
    }
}
