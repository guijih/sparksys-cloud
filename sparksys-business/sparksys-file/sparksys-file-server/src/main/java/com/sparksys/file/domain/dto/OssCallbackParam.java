package com.sparksys.file.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: oss上传成功后的回调参数
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:36:52
 */
@Data
@ApiModel(description = "oss上传成功后的回调参数")
public class OssCallbackParam {

    @ApiModelProperty("请求的回调地址")
    private String callbackUrl;

    @ApiModelProperty("回调是传入request中的参数")
    private String callbackBody;

    @ApiModelProperty("回调时传入参数的格式，比如表单提交形式")
    private String callbackBodyType;
}
