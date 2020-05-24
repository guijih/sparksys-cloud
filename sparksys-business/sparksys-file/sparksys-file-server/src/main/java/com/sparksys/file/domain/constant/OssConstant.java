package com.sparksys.file.domain.constant;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

/**
 * description: oss属性常量
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:35:57
 */
public class OssConstant {

    public final static String PROTOCOL_HTTPS = "https://";


    public final static String URL_SEPARATOR = "/";


    public static final Map<String, String> OSS_EXCEPTION_MAP =
            ImmutableMap.<String, String>builder().
                    put("NoSuchKey", "文件不存在")
                    .build();

}
