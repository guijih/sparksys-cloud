package com.sparksys.gateway.infrastructure.prop;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * description: 用于配置不需要保护的资源路径
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:17:10
 */
@Getter
@Setter
@Component
@RefreshScope
@ConfigurationProperties(prefix = "security.ignored")
@Slf4j
public class IgnoreUrlsProperties {

    private List<String> urls;

}
