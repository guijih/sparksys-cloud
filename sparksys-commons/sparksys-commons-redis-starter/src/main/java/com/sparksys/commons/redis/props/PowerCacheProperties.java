package com.sparksys.commons.redis.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description: 缓存构建配置
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:33:14
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.power")
public class PowerCacheProperties {

    private boolean useLocalCache;

    private boolean useRedisCache;

}
