package com.sparksys.authorization.infrastructure.component;

import com.sparksys.commons.core.entity.AuthUser;
import com.sparksys.commons.redis.cache.CacheProviderService;
import com.sparksys.commons.web.service.AbstractAuthSecurityRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * projectName：parent
 * packageName：com.primus.mall.security.service
 * description：获取用户信息service
 *
 * @Author zhoux
 * @Date 2020/1/3 0003 11:07
 */
@Service
@Slf4j
public class AuthSecurityRequest extends AbstractAuthSecurityRequest {

    private final CacheProviderService cacheProviderService;

    public AuthSecurityRequest(@Qualifier("redisCacheProvider") CacheProviderService cacheProviderService) {
        this.cacheProviderService = cacheProviderService;
    }


    @Override
    protected AuthUser getCache(String key) {
        return cacheProviderService.get(key);
    }
}
