package com.sparksys.file.web;

import com.sparksys.commons.core.support.BusinessException;
import com.sparksys.commons.web.annotation.ResponseResult;
import com.sparksys.file.application.command.IFileCommandService;
import com.sparksys.file.application.query.IFileQueryService;
import com.sparksys.file.domain.dto.OssPolicyResult;
import com.sparksys.file.domain.model.FileMaterial;
import com.sparksys.file.domain.dto.FileDTO;
import com.sparksys.file.domain.dto.OssCallbackDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * description: 文件上传 前端控制器
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:40:10
 */
@RestController
@ResponseResult
@RequestMapping("/file")
@Api(tags = "文件管理")
public class FileController {

    private final IFileCommandService fileCommandService;
    private final IFileQueryService fileQueryService;

    public FileController(IFileCommandService fileCommandService,
                          IFileQueryService fileQueryService) {
        this.fileCommandService = fileCommandService;
        this.fileQueryService = fileQueryService;
    }

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public FileMaterial upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        return fileCommandService.upload(multipartFile);
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/delete/{fileName}")
    public boolean delete(@PathVariable("fileName") String fileName) throws BusinessException {
        return fileCommandService.deleteFile(fileName);
    }

    @ApiOperation("转换html文件")
    @PostMapping("/html")
    public FileDTO getHtml(@RequestBody FileDTO fileDTO) throws Exception {
        return fileCommandService.getHtml(fileDTO);
    }

    @ApiOperation("获取oss配置信息")
    @GetMapping("/ossPolicy")
    public OssPolicyResult ossPolicy() {
        return fileQueryService.policy();
    }

    @ApiOperation("文件上传回调")
    @PostMapping("/callback")
    public FileMaterial callback(@RequestBody OssCallbackDTO ossCallbackDTO) {
        return fileCommandService.callback(ossCallbackDTO);
    }
}
