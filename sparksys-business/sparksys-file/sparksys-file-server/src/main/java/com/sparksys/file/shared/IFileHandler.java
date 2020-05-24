package com.sparksys.file.shared;

import com.aliyun.oss.model.OSSObject;
import com.sparksys.commons.core.support.BusinessException;
import com.sparksys.file.domain.dto.OssPolicyResult;
import com.sparksys.file.domain.model.UploadResult;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * description: File handler interface
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:34:47
 */
public interface IFileHandler {

    MediaType IMAGE_TYPE = MediaType.valueOf("image/*");

    /**
     * 判断是否是图片类型
     *
     * @param mediaType
     * @return
     */
    static boolean isImageType(@Nullable MediaType mediaType) {
        return mediaType != null && IMAGE_TYPE.includes(mediaType);
    }


    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    UploadResult upload(MultipartFile file) throws Exception;


    /**
     * 上传本地文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    UploadResult upload(File file) throws Exception;

    /**
     * 删除文件
     *
     * @param key
     * @throws BusinessException
     */
    void delete(String key) throws BusinessException;

    /**
     * 获取文件
     *
     * @param fileName
     * @return
     * @throws BusinessException
     */
    OSSObject getFile(String fileName) throws BusinessException;

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     * @throws BusinessException
     */
    boolean exist(String fileName) throws BusinessException;

    /**
     * 签名生成
     *
     * @return
     */
    OssPolicyResult policy();
}
