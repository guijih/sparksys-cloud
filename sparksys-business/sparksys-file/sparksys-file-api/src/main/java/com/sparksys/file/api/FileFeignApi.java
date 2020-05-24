package com.sparksys.file.api;

import com.sparksys.commons.core.api.result.ApiResult;
import com.sparksys.file.common.domain.dto.FileDTO;
import com.sparksys.file.hystrix.FileFeignApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * description: 文件API
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:27:04
 */
@FeignClient(value = "sparksys-file", path = "/file",
        qualifier = "fileFeignApi", fallback = FileFeignApiFallback.class)
public interface FileFeignApi {

    /**
     * 转换pdf文件
     *
     * @param fileDTO 文件入参
     * @return
     */
    @PostMapping("/pdf")
    ApiResult<FileDTO> getPdf(@RequestBody FileDTO fileDTO);


    /**
     * 转换HTML文件
     *
     * @param fileDTO 文件入参
     * @return
     */
    @PostMapping("/html")
    ApiResult<FileDTO> getHtml(@RequestBody FileDTO fileDTO);
}
