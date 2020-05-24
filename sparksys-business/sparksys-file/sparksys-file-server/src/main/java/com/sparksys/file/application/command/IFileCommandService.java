package com.sparksys.file.application.command;

import com.sparksys.commons.core.support.BusinessException;
import com.sparksys.file.domain.dto.FileDTO;
import com.sparksys.file.domain.dto.OssCallbackDTO;
import com.sparksys.file.domain.model.FileMaterial;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * description: 文件上传服务
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:30:30
 */
public interface IFileCommandService {

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @return
     * @throws Exception
     */
    FileMaterial upload(MultipartFile multipartFile) throws Exception;

    /**
     * 删除文件
     *
     * @param objectName 文件名
     * @return
     * @throws BusinessException
     */
    boolean deleteFile(String objectName) throws BusinessException;

    /**
     * 文件上传回调
     *
     * @param ossCallbackDTO 回调参数
     * @return
     */
    FileMaterial callback(OssCallbackDTO ossCallbackDTO);

    /**
     * 转换html文件
     *
     * @param fileDTO 文件入参
     * @return
     * @throws Exception
     */
    FileDTO getHtml(FileDTO fileDTO) throws Exception;
}
