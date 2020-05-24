package com.sparksys.file.common.domain.dto;

import java.util.List;

import lombok.Data;

/**
 * description: 文件入参
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:30:13
 */
@Data
public class FileDTO {

    private String filePath;

    private List<String> fileList;

}
