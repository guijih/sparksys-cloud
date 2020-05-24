package com.sparksys.file.infrastructure.upload;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.sparksys.commons.core.support.BusinessException;
import com.sparksys.commons.core.utils.file.FileUtil;
import com.sparksys.commons.core.utils.file.FilenameUtils;
import com.sparksys.file.domain.constant.OssConstant;
import com.sparksys.file.domain.dto.OssCallbackParam;
import com.sparksys.file.domain.dto.OssPolicyResult;
import com.sparksys.file.domain.model.UploadResult;
import com.sparksys.file.infrastructure.prop.OssProperties;
import com.sparksys.file.shared.IFileHandler;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * description: aliYun oss 文件上传处理器
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:34:06
 */
@Slf4j
@Component
public class AliOssFileHandler implements IFileHandler {


    private final OssProperties ossProperties;
    private StringBuilder basePath;

    public AliOssFileHandler(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @PostConstruct
    public void initBasePath() {
        String domain = ossProperties.getDomain();
        String endPoint = ossProperties.getEndpoint();
        String bucketName = ossProperties.getBucketName();
        basePath = new StringBuilder();
        if (StringUtils.isNotEmpty(domain)) {
            basePath.append(OssConstant.PROTOCOL_HTTPS)
                    .append(domain)
                    .append(OssConstant.URL_SEPARATOR);
        } else {
            basePath.append(OssConstant.PROTOCOL_HTTPS)
                    .append(bucketName)
                    .append(".")
                    .append(endPoint)
                    .append(OssConstant.URL_SEPARATOR);
        }
    }

    @Override
    public UploadResult upload(MultipartFile file) throws Exception {
        return aliOssUpload(file);
    }

    @Override
    public UploadResult upload(File file) throws Exception {
        return aliOssUpload(file);
    }


    private UploadResult aliOssUpload(MultipartFile multipartFile) throws BusinessException {
        // Init OSS client
        OSS ossClient = getOssClient();
        String originalFilename = multipartFile.getOriginalFilename();
        try {
            InputStream inputStream = multipartFile.getInputStream();
            assert originalFilename != null;
            String extension = FilenameUtils.getExtension(originalFilename);
            String basename = FilenameUtils.getBasename(originalFilename);
            String uploadFilePath = uploadFilePath(originalFilename);
            String filePath = filePath(uploadFilePath);

            log.info("文件路径为{}", filePath);
            // Upload
            PutObjectResult putObjectResult = ossClient.putObject(ossProperties.getBucketName(),
                    uploadFilePath, inputStream);
            if (putObjectResult == null) {
                throw new BusinessException(
                        "上传附件 " + multipartFile.getOriginalFilename() + " 到阿里云失败 ");
            }
            // Response result
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(basename);
            uploadResult.setFilePath(filePath);
            uploadResult.setKey(uploadFilePath);
            uploadResult.setMediaType(
                    MediaType.valueOf(Objects.requireNonNull(multipartFile.getContentType())));
            uploadResult.setSuffix(extension);
            uploadResult.setSize(multipartFile.getSize());
            log.info("Uploaded file: [{}] successfully", originalFilename);
            return uploadResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("上传附件 " + originalFilename + " 失败 ");
        } finally {
            ossClient.shutdown();
        }
    }


    public String filePath(String uploadFilePath) {
        return StringUtils.join(basePath.toString(), uploadFilePath);
    }

    public String uploadFilePath(String originalFilename) {
        String source = ossProperties.getSource();
        String extension = FilenameUtils.getExtension(originalFilename);
        String basename = FilenameUtils.getBasename(originalFilename);
        StrBuilder strBuilder = new StrBuilder();
        if (StringUtils.isNotEmpty(source)) {
            strBuilder.append(source)
                    .append(OssConstant.URL_SEPARATOR);
        }
        return strBuilder.append(basename).append(".").append(extension).toString();
    }

    private UploadResult aliOssUpload(File file) throws BusinessException {
        // Init OSS client
        OSS ossClient = getOssClient();
        String originalFilename = file.getName();
        try {
            String uploadFilePath = uploadFilePath(originalFilename);
            String filePath = filePath(uploadFilePath);
            log.info("文件路径为{}", filePath);
            // Upload
            PutObjectResult putObjectResult = ossClient.putObject(ossProperties.getBucketName(),
                    uploadFilePath, file);
            if (putObjectResult == null) {
                throw new BusinessException("上传附件 " + originalFilename + " 到阿里云失败 ");
            }
            // Response result
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(originalFilename);
            uploadResult.setFilePath(filePath);
            uploadResult.setKey(uploadFilePath);
            uploadResult.setSuffix(FilenameUtils.getExtension(originalFilename));
            uploadResult.setSize(file.length());
            log.info("Uploaded file1: [{}] successfully", originalFilename);
            return uploadResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("上传附件 " + originalFilename + " 失败 ");
        } finally {
            ossClient.shutdown();
            file.delete();
        }
    }

    @Override
    public void delete(String key) throws BusinessException {
        Assert.notNull(key, "File key must not be blank");
        String bucketName = ossProperties.getBucketName();
        // Init OSS client
        OSS ossClient = getOssClient();
        try {
            ossClient.deleteObject(new DeleteObjectsRequest(bucketName).withKey(key));
        } catch (Exception e) {
            throw new BusinessException("附件 " + key + " 从阿里云删除失败");
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public OSSObject getFile(String fileName) {
        String bucketName = ossProperties.getBucketName();
        // Init OSS client
        OSS ossClient = getOssClient();
        OSSObject ossObject = null;
        try {
            ossObject = ossClient.getObject(bucketName, fileName);
        } catch (OSSException e) {
            log.error("oss exception is {}", OssConstant.OSS_EXCEPTION_MAP.get(e.getErrorCode()));
        } finally {
            ossClient.shutdown();
        }
        return ossObject;
    }

    @Override
    public boolean exist(String fileName) {
        return getFile(fileName) != null;
    }


    /**
     * 签名生成
     */
    @Override
    public OssPolicyResult policy() {
        OssPolicyResult result = new OssPolicyResult();
        // 存储目录
        String dir = ossProperties.getSource();
        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + ossProperties.getExpire() * 1000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        long maxSize = ossProperties.getMaxSize() * 1024 * 1024;
        // 回调
        OssCallbackParam callback = new OssCallbackParam();
        callback.setCallbackUrl(ossProperties.getCallback());
        callback.setCallbackBody(
                "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.setCallbackBodyType("application/x-www-form-urlencoded");
        // 提交节点
        OSS ossClient = getOssClient();
        try {
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            String callbackData = BinaryUtil.toBase64String(
                    JSONUtil.parse(callback).toString().getBytes(StandardCharsets.UTF_8));
            // 返回结果
            result.setAccessKeyId(ossProperties.getAccessKeyId());
            result.setPolicy(policy);
            result.setSignature(signature);
            result.setDir(dir);
            result.setCallback(callbackData);
            result.setHost(basePath.toString());
        } catch (Exception e) {
            log.error("签名生成失败", e);
        }
        return result;
    }


    private OSS getOssClient() {
        return new OSSClientBuilder().build(ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret());
    }
}

