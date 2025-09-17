package com.baitabei.service;

import com.baitabei.common.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件服务接口
 * 
 * @author MiniMax Agent
 */
public interface FileService {
    /**
     * 同时生成预览（inline）与下载（attachment）两条签名 URL
     */
    Result<Map<String, String>> generateBothUrls(String objectKey);
    /**
     * 上传单个文件
     * @param file 文件
     * @param userId 用户ID
     * @return 文件URL
     */
    Result<String> uploadFile(MultipartFile file, Long userId);

    /**
     * 批量上传文件
     * @param files 文件列表
     * @param userId 用户ID
     * @return 文件URL列表
     */
    Result<List<String>> uploadFiles(MultipartFile[] files, Long userId);

    /**
     * 上传图片
     * @param file 图片文件
     * @param userId 用户ID
     * @return 图片URL
     */
    Result<String> uploadImage(MultipartFile file, Long userId);

    /**
     * 上传视频
     * @param file 视频文件
     * @param userId 用户ID
     * @return 视频URL
     */
    Result<String> uploadVideo(MultipartFile file, Long userId);

    /**
     * 上传文档
     * @param file 文档文件
     * @param userId 用户ID
     * @return 文档URL
     */
    Result<String> uploadDocument(MultipartFile file, Long userId);

    /**
     * 上传项目文件
     * @param file 文件
     * @param projectId 项目ID
     * @param userId 用户ID
     * @param category 文件分类
     * @param description 文件描述
     * @return 上传结果
     */
    Result<Long> uploadProjectFile(MultipartFile file, Long projectId, Long userId, String category, String description);

    /**
     * 删除文件
     * @param fileUrl 文件URL
     * @param userId 用户ID
     * @return 删除结果
     */
    Result<Void> deleteFile(String fileUrl, Long userId);

    /**
     * 删除项目文件
     * @param fileId 文件ID
     * @param userId 用户ID
     * @return 删除结果
     */
    Result<Void> deleteProjectFile(Long fileId, Long userId);

    /**
     * 验证文件类型
     * @param fileName 文件名
     * @param allowedTypes 允许的文件类型
     * @return 是否允许
     */
    boolean isAllowedFileType(String fileName, String[] allowedTypes);

    /**
     * 验证文件大小
     * @param fileSize 文件大小
     * @param maxSize 最大大小
     * @return 是否允许
     */
    boolean isAllowedFileSize(long fileSize, long maxSize);

    /**
     * 获取文件扩展名
     * @param fileName 文件名
     * @return 扩展名
     */
    String getFileExtension(String fileName);

    /**
     * 生成唯一文件名
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    String generateUniqueFileName(String originalFileName);
}
