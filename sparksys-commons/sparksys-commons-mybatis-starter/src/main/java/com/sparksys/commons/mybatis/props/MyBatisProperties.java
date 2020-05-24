package com.sparksys.commons.mybatis.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description: mybatis配置属性常量
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:23:12
 */
@Data
@Component
@ConfigurationProperties(prefix = "mybatis")
public class MyBatisProperties {

    private String mapperLocations;

    private String typeAliasesPackage;
}
