package com.sparksys.file.infrastructure.prop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * description: oss属性配置
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:39:43
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@RefreshScope
@ApiModel(description = "阿里云oss配置")
public class OssProperties {

    @ApiModelProperty(value = "域名代理")
    private String domain;

    @ApiModelProperty(value = "地域节点")
    private String endpoint;

    @ApiModelProperty(value = "Access Key")
    private String accessKeyId;

    @ApiModelProperty(value = "Access Secret")
    private String accessKeySecret;

    @ApiModelProperty(value = "存储桶")
    private String bucketName;

    @ApiModelProperty(value = "存储目录")
    private String source;

    @ApiModelProperty(value = "回调地址")
    private String callback;

    @ApiModelProperty(value = "有效期")
    private int expire;

    @ApiModelProperty(value = "上传大小")
    private int maxSize;

}
