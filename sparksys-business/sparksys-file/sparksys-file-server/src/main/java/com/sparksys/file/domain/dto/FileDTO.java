package com.sparksys.file.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description: 文件入参
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:36:23
 */
@Data
@ApiModel(value = "文件入参", description = "文件入参")
public class FileDTO {

    @ApiModelProperty(value = "文件地址")
    private String filePath;

    @ApiModelProperty(value = "文件地址列表")
    private List<String> fileList;

}
