package com.sparksys.commons.zookeeper.config;

import com.sparksys.commons.zookeeper.lock.DistributedZkLock;
import com.sparksys.commons.zookeeper.prop.CuratorProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description:
 *
 * @Author zhouxinlei
 * @Date  2020-05-24 13:45:22
 */
@Configuration
public class CuratorConfig {

    @Autowired
    private CuratorProperties curatorProperties;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                curatorProperties.getUrl(),
                curatorProperties.getSessionTimeoutMs(),
                curatorProperties.getConnectionTimeoutMs(),
                new RetryNTimes(curatorProperties.getRetryCount(), curatorProperties.getElapsedTimeMs()));
    }

    @Bean
    public DistributedZkLock distributedZkLock(CuratorFramework curatorFramework) {
        return new DistributedZkLock(curatorFramework);
    }
}
