package com.sparksys.file.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * description: oss上传回调入参
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:36:34
 */
@Data
@ApiModel(description = "oss上传回调入参")
public class OssCallbackDTO {

    private String filename;

    private Double size;

    private String contentType;

    private String filePath;
}
