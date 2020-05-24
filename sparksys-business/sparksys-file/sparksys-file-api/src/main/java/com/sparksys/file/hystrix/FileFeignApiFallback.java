package com.sparksys.file.hystrix;

import com.sparksys.commons.core.api.result.ApiResult;
import com.sparksys.file.api.FileFeignApi;
import com.sparksys.file.common.domain.dto.FileDTO;
import org.springframework.stereotype.Component;

/**
 * description:文件API降级处理
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:29:26
 */
@Component
public class FileFeignApiFallback implements FileFeignApi {
    @Override
    public ApiResult<FileDTO> getPdf(FileDTO fileDTO) {
        return ApiResult.timeOut();
    }

    @Override
    public ApiResult<FileDTO> getHtml(FileDTO fileDTO) {
        return ApiResult.timeOut();
    }
}
