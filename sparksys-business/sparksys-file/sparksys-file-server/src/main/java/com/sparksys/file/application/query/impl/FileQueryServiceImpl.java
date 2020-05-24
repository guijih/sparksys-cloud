package com.sparksys.file.application.query.impl;

import com.sparksys.file.application.query.IFileQueryService;
import com.sparksys.file.domain.dto.OssPolicyResult;
import com.sparksys.file.infrastructure.upload.AliOssFileHandler;
import org.springframework.stereotype.Service;

/**
 * description: 文件查询服务实现类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:33:15
 */
@Service
public class FileQueryServiceImpl implements IFileQueryService {

    private final AliOssFileHandler aliOssFileHandler;

    public FileQueryServiceImpl(
            AliOssFileHandler aliOssFileHandler) {
        this.aliOssFileHandler = aliOssFileHandler;
    }

    @Override
    public OssPolicyResult policy() {
        return aliOssFileHandler.policy();
    }
}
