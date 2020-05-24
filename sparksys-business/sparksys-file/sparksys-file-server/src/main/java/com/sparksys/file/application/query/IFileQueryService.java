package com.sparksys.file.application.query;

import com.sparksys.file.domain.dto.OssPolicyResult;

/**
 * description: 文件查询服务
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:32:56
 */
public interface IFileQueryService {


    /**
     * 获取oss配置信息
     *
     * @return
     */
    OssPolicyResult policy();

}
