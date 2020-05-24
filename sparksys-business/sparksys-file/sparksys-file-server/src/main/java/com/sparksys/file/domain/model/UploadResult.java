package com.sparksys.file.domain.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.http.MediaType;

/**
 * description: 上传结果
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:38:34
 */
@Data
@ToString
public class UploadResult {

    private String filename;

    private String filePath;

    private String key;

    private String thumbPath;

    private String suffix;

    private MediaType mediaType;

    private Long size;

    private Integer width;

    private Integer height;

}
