package com.baitabei.controller;

import com.baitabei.common.result.Result;
import com.baitabei.common.result.ResultCode;
import com.baitabei.security.UserPrincipal;
import com.baitabei.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件上传控制器
 * 
 * @author MiniMax Agent
 */
@Tag(name = "文件管理", description = "文件管理")
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 一次性返回预览 + 下载两个签名地址
     */
    @Operation(summary = "预览文件")
    @GetMapping("/preview")
    public Result<Map<String, String>> getSignedUrls(@RequestParam String objectKey) {
        return fileService.generateBothUrls(objectKey);
    }

    @Operation(summary = "上传单个文件")
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return fileService.uploadFile(file, userPrincipal.getId());
    }

    @Operation(summary = "批量上传文件")
    @PostMapping("/upload-batch")
    public Result<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return fileService.uploadFiles(files, userPrincipal.getId());
    }

    @Operation(summary = "上传图片")
    @PostMapping("/upload-image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return fileService.uploadImage(file, userPrincipal.getId());
    }

    @Operation(summary = "上传视频")
    @PostMapping("/upload-video")
    public Result<String> uploadVideo(@RequestParam("file") MultipartFile file,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return fileService.uploadVideo(file, userPrincipal.getId());
    }

    @Operation(summary = "上传文档")
    @PostMapping("/upload-document")
    public Result<String> uploadDocument(@RequestParam("file") MultipartFile file,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return fileService.uploadDocument(file, userPrincipal.getId());
    }

    @Operation(summary = "上传项目文件")
    @PostMapping("/upload-project-file")
    public Result<Long> uploadProjectFile(@RequestPart("file") MultipartFile file,
                                         @RequestParam("projectId") Long projectId,
                                         @RequestParam("category") String category,
                                         @Parameter(description = "description")@RequestParam(required = false) String description,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return fileService.uploadProjectFile(file, projectId, userPrincipal.getId(), category, description);
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/delete")
    public Result<Void> deleteFile(@RequestParam("fileUrl") String fileUrl,
                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return fileService.deleteFile(fileUrl, userPrincipal.getId());
    }

    @Operation(summary = "删除项目文件")
    @DeleteMapping("/delete-project-file/{fileId}")
    public Result<Void> deleteProjectFile(@PathVariable Long fileId,
                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return fileService.deleteProjectFile(fileId, userPrincipal.getId());
    }
}
