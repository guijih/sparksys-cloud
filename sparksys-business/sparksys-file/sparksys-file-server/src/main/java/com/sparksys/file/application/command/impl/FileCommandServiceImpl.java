package com.sparksys.file.application.command.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sparksys.commons.core.support.BusinessException;
import com.sparksys.commons.core.utils.file.FilenameUtils;
import com.sparksys.commons.core.utils.pdf.Mht2HtmlUtil;
import com.sparksys.file.application.command.IFileCommandService;
import com.sparksys.file.domain.dto.FileDTO;
import com.sparksys.file.domain.dto.OssCallbackDTO;
import com.sparksys.file.domain.mapper.FileMaterialMapper;
import com.sparksys.file.domain.model.FileMaterial;
import com.sparksys.file.domain.model.UploadResult;
import com.sparksys.file.infrastructure.upload.AliOssFileHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.Objects;

/**
 * description: 文件上传服务实现类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:32:31
 */
@Service
@Slf4j
public class FileCommandServiceImpl implements IFileCommandService {

    @Autowired
    private AliOssFileHandler aliOssFileHandler;
    @Autowired
    private FileMaterialMapper fileMaterialMapper;

    @Override
    public FileMaterial upload(MultipartFile multipartFile) throws Exception {
        FileMaterial fileMaterial;
        //文件新路径
        String fileName = FilenameUtils.getBasename(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        QueryWrapper<FileMaterial> materialQueryWrapper = new QueryWrapper<>();
        materialQueryWrapper.eq("file_name", fileName);
        materialQueryWrapper.eq("suffix", extension);
        fileMaterial = fileMaterialMapper.selectOne(materialQueryWrapper);
        if (ObjectUtils.isEmpty(fileMaterial)) {
            fileMaterial = new FileMaterial();
            // 上传到阿里云
            UploadResult uploadResult = aliOssFileHandler.upload(multipartFile);
            fileMaterial.setFileName(uploadResult.getFilename());
            fileMaterial.setSuffix(uploadResult.getSuffix());
            fileMaterial.setFilePath(uploadResult.getFilePath());
            fileMaterial.setSize((double) uploadResult.getSize());
            fileMaterial.setContentType(multipartFile.getContentType());
            fileMaterial.setUid(String.valueOf(System.currentTimeMillis()));
            fileMaterial.setCreateTime(new Date());
            fileMaterialMapper.insert(fileMaterial);
            log.info("文件上传成功 = {}", JSONObject.toJSONString(fileMaterial));
        }
        return fileMaterial;
    }

    @Override
    public boolean deleteFile(String fileName) throws BusinessException {
        // 根据BucketName,objectName删除文件
        aliOssFileHandler.delete(fileName);
        QueryWrapper<FileMaterial> materialQueryWrapper = new QueryWrapper<>();
        materialQueryWrapper.eq("file_name", fileName);
        return fileMaterialMapper.delete(materialQueryWrapper) != 0;
    }

    @Override
    public FileMaterial callback(OssCallbackDTO ossCallbackDTO) {
        FileMaterial fileMaterial;
        //文件新路径
        QueryWrapper<FileMaterial> materialQueryWrapper = new QueryWrapper<>();
        materialQueryWrapper.eq("file_path", ossCallbackDTO.getFilePath());
        fileMaterial = fileMaterialMapper.selectOne(materialQueryWrapper);
        if (ObjectUtils.isEmpty(fileMaterial)) {
            fileMaterial = new FileMaterial();
            String filePath = ossCallbackDTO.getFilePath();
            String extension = FilenameUtils.getExtension(ossCallbackDTO.getFilename());
            fileMaterial.setFileName(ossCallbackDTO.getFilename());
            fileMaterial.setSuffix(extension);
            fileMaterial.setFilePath(filePath);
            fileMaterial.setSize(ossCallbackDTO.getSize());
            fileMaterial.setContentType(ossCallbackDTO.getContentType());
            fileMaterial.setUid(String.valueOf(System.currentTimeMillis()));
            fileMaterial.setCreateTime(new Date());
            fileMaterialMapper.insert(fileMaterial);
            log.info("文件记录成功 = {}", JSONObject.toJSONString(fileMaterial));
        }
        return fileMaterial;
    }

    @Override
    public FileDTO getHtml(FileDTO fileDTO) throws Exception {
        String fileName = fileDTO.getFilePath().substring(fileDTO.getFilePath().lastIndexOf('/') + 1);
        String baseName = FilenameUtils.getBasename(fileName);
        String tempFilePath = "/data/" + baseName + ".html";
        Mht2HtmlUtil.mht2html(fileDTO.getFilePath(), tempFilePath);
        File file = new File(tempFilePath);
        UploadResult uploadResult = aliOssFileHandler.upload(file);
        FileDTO outDto = new FileDTO();
        outDto.setFilePath(uploadResult.getFilePath());
        file.delete();
        return outDto;
    }
}
