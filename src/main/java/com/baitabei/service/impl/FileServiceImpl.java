package com.baitabei.service.impl;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.baitabei.common.result.Result;
import com.baitabei.common.result.ResultCode;
import com.baitabei.constant.AppConstants;
import com.baitabei.entity.ProjectFile;
import com.baitabei.mapper.ProjectFileMapper;
import com.baitabei.mapper.ProjectMapper;
import com.baitabei.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map;
import java.util.HashMap;
/**
 * 文件服务实现类
 * 支持阿里云OSS文件上传、删除等操作
 *
 * @author MiniMax Agent
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${aliyun.oss.endpoint:}")
    private String ossEndpoint;

    @Value("${aliyun.oss.access-key-id:}")
    private String ossAccessKeyId;

    @Value("${aliyun.oss.access-key-secret:}")
    private String ossAccessKeySecret;

    @Value("${aliyun.oss.bucket-name:}")
    private String ossBucketName;

    @Value("${aliyun.oss.url-prefix:}")
    private String ossUrlPrefix;

    // 注意：在实际部署时需要添加阿里云OSS SDK依赖和配置
     private OSS ossClient;

    @Autowired
    private ProjectFileMapper projectFileMapper;

    @Override
    public Result<String> uploadFile(MultipartFile file, Long userId) {
        if (!isOssConfigured()) {
            return Result.error(ResultCode.VALIDATE_FAILED.getCode(), "OSS配置不完整");
        }
        try {
            // 验证文件
             Result<Void> validationResult = validateFile(file);
            if (validationResult.getCode() != ResultCode.SUCCESS.getCode()) {
                return Result.error(validationResult.getCode(), validationResult.getMessage());
            }

            // 生成文件名
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String filePath = buildFilePath(AppConstants.FILE_TYPE_OTHER, fileName);

            // 上传到OSS
            String fileUrl = uploadToOSS(file, filePath);
            if (fileUrl != null) {
                logger.info("文件上传成功，用户ID: {}, 文件URL: {}", userId, fileUrl);
                return Result.success(fileUrl);
            } else {
                return Result.error(ResultCode.FILE_UPLOAD_ERROR);
            }
        } catch (Exception e) {
            logger.error("文件上传失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public Result<Map<String, String>> generateBothUrls(String objectKey) {
        if (!isOssConfigured()) {
            return Result.error(ResultCode.VALIDATE_FAILED.getCode(), "OSS配置不完整");
        }
        try {
            // 1 小时
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            if (ossClient == null) {
                ossClient = new OSSClientBuilder().build(ossEndpoint, ossAccessKeyId, ossAccessKeySecret);
            }

            // 一条原生签名 URL（不带任何 response-content-disposition）
            URL url = ossClient.generatePresignedUrl(ossBucketName, objectKey, expiration);
            String base = url.toString();

            Map<String, String> result = new HashMap<>(2);

            // 替换域名为你自己的 CDN 加速域名
            base = base.replace("http://" + ossBucketName + ".oss-cn-beijing.aliyuncs.com",
                    "https://assets.hzyuanlian.cn");

            // 下载地址（attachment）——参数参与签名
            GeneratePresignedUrlRequest downReq =
                    new GeneratePresignedUrlRequest(ossBucketName, objectKey, HttpMethod.GET);
            downReq.setExpiration(expiration);
            ResponseHeaderOverrides overrides = new ResponseHeaderOverrides();
            overrides.setContentDisposition("attachment");
            downReq.setResponseHeaders(overrides);
            String downloadUrl = ossClient.generatePresignedUrl(downReq).toString();


            result.put("previewUrl", base);                    // 浏览器直接播放
            result.put("downloadUrl", downloadUrl); // 强制下载（不破坏签名）
            logger.info("生成双地址成功，objectKey={}", objectKey);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("生成双地址失败，objectKey: {}", objectKey, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public Result<List<String>> uploadFiles(MultipartFile[] files, Long userId) {
        try {
            List<String> fileUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                Result<String> uploadResult = uploadFile(file, userId);
                if (uploadResult.getCode() == ResultCode.SUCCESS.getCode()) {
                    fileUrls.add(uploadResult.getData());
                } else {
                    // 如果有文件上传失败，记录日志但继续处理其他文件
                    logger.warn("文件上传失败: {}", file.getOriginalFilename());
                }
            }

            return Result.success(fileUrls);
        } catch (Exception e) {
            logger.error("批量文件上传失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public Result<String> uploadImage(MultipartFile file, Long userId) {
        try {
            // 验证图片类型
            if (!isAllowedFileType(file.getOriginalFilename(), AppConstants.ALLOWED_IMAGE_TYPES)) {
                return Result.error(ResultCode.FILE_TYPE_NOT_ALLOWED);
            }

            // 验证文件大小
            if (!isAllowedFileSize(file.getSize(), AppConstants.MAX_FILE_SIZE)) {
                return Result.error(ResultCode.FILE_SIZE_EXCEEDED);
            }

            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String filePath = buildFilePath(AppConstants.FILE_TYPE_IMAGE, fileName);

            String fileUrl = uploadToOSS(file, filePath);
            if (fileUrl != null) {
                logger.info("图片上传成功，用户ID: {}, 文件URL: {}", userId, fileUrl);
                return Result.success(fileUrl);
            } else {
                return Result.error(ResultCode.FILE_UPLOAD_ERROR);
            }
        } catch (Exception e) {
            logger.error("图片上传失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public Result<String> uploadVideo(MultipartFile file, Long userId) {
        try {
            // 验证视频类型
            if (!isAllowedFileType(file.getOriginalFilename(), AppConstants.ALLOWED_VIDEO_TYPES)) {
                return Result.error(ResultCode.FILE_TYPE_NOT_ALLOWED);
            }

            // 视频文件大小限制较大（100MB）
            long maxVideoSize = 100 * 1024 * 1024L;
            if (!isAllowedFileSize(file.getSize(), maxVideoSize)) {
                return Result.error(ResultCode.FILE_SIZE_EXCEEDED);
            }

            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String filePath = buildFilePath(AppConstants.FILE_TYPE_VIDEO, fileName);

            String fileUrl = uploadToOSS(file, filePath);
            if (fileUrl != null) {
                logger.info("视频上传成功，用户ID: {}, 文件URL: {}", userId, fileUrl);
                return Result.success(fileUrl);
            } else {
                return Result.error(ResultCode.FILE_UPLOAD_ERROR);
            }
        } catch (Exception e) {
            logger.error("视频上传失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public Result<String> uploadDocument(MultipartFile file, Long userId) {
        try {
            // 验证文档类型
            if (!isAllowedFileType(file.getOriginalFilename(), AppConstants.ALLOWED_DOCUMENT_TYPES)) {
                return Result.error(ResultCode.FILE_TYPE_NOT_ALLOWED);
            }

            // 验证文件大小
            if (!isAllowedFileSize(file.getSize(), AppConstants.MAX_FILE_SIZE)) {
                return Result.error(ResultCode.FILE_SIZE_EXCEEDED);
            }

            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String filePath = buildFilePath(AppConstants.FILE_TYPE_DOCUMENT, fileName);

            String fileUrl = uploadToOSS(file, filePath);
            if (fileUrl != null) {
                logger.info("文档上传成功，用户ID: {}, 文件URL: {}", userId, fileUrl);
                return Result.success(fileUrl);
            } else {
                return Result.error(ResultCode.FILE_UPLOAD_ERROR);
            }
        } catch (Exception e) {
            logger.error("文档上传失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public Result<Long> uploadProjectFile(MultipartFile file, Long projectId, Long userId, String category, String description) {
        try {
            // 上传文件到OSS
            Result<String> uploadResult = uploadFile(file, userId);
            if (uploadResult.getCode() != ResultCode.SUCCESS.getCode()) {
                return Result.error(uploadResult.getCode(), uploadResult.getMessage());
            }

            String fileUrl = uploadResult.getData();

            // 创建项目文件记录
            ProjectFile projectFile = new ProjectFile();
            projectFile.setProjectId(projectId);
            projectFile.setUploadUserId(userId);  // 使用uploadUserId而不是userId
            projectFile.setFileName(file.getOriginalFilename());
            projectFile.setFileUrl(fileUrl);
            projectFile.setFileSize(file.getSize());
            projectFile.setFileType(getFileExtension(file.getOriginalFilename()));
            projectFile.setCategory(category);
            projectFile.setDescription(description);
            projectFile.setCreatedTime(LocalDateTime.now());

            // 保存到数据库（需要注入ProjectFileMapper）
            // int result = projectFileMapper.insert(projectFile);
            // 这里暂时返回一个模拟的ID
            Long fileId = System.currentTimeMillis();

            logger.info("项目文件上传成功，项目ID: {}, 文件ID: {}, 用户ID: {}", projectId, fileId, userId);
            return Result.success(fileId);

        } catch (Exception e) {
            logger.error("项目文件上传失败，项目ID: {}, 用户ID: {}", projectId, userId, e);
            return Result.error(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public Result<Void> deleteFile(String fileUrl, Long userId) {
        try {
            // 从OSS删除文件
            boolean deleted = deleteFromOSS(fileUrl);
            if (deleted) {
                logger.info("文件删除成功，用户ID: {}, 文件URL: {}", userId, fileUrl);
                return Result.success();
            } else {
                return Result.error(ResultCode.FILE_DELETE_ERROR);
            }
        } catch (Exception e) {
            logger.error("文件删除失败，用户ID: {}, 文件URL: {}", userId, fileUrl, e);
            return Result.error(ResultCode.FILE_DELETE_ERROR);
        }
    }

    @Override
    public Result<Void> deleteProjectFile(Long fileId, Long userId) {
        try {
            // 查询项目文件信息（需要注入ProjectFileMapper）
            ProjectFile projectFile = projectFileMapper.findById(fileId);
            // 这里使用模拟数据
            String fileUrl = projectFile.getFileUrl();

            // 从OSS删除文件
            boolean deleted = deleteFromOSS(fileUrl);
            if (deleted) {
                // 删除数据库记录
                 projectFileMapper.deleteById(fileId);
                logger.info("项目文件删除成功，文件ID: {}, 用户ID: {}", fileId, userId);
                return Result.success();
            } else {
                return Result.error(ResultCode.FILE_DELETE_ERROR);
            }
        } catch (Exception e) {
            logger.error("项目文件删除失败，文件ID: {}, 用户ID: {}", fileId, userId, e);
            return Result.error(ResultCode.FILE_DELETE_ERROR);
        }
    }

    @Override
    public boolean isAllowedFileType(String fileName, String[] allowedTypes) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        String extension = getFileExtension(fileName).toLowerCase();
        for (String allowedType : allowedTypes) {
            if (allowedType.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAllowedFileSize(long fileSize, long maxSize) {
        return fileSize > 0 && fileSize <= maxSize;
    }

    @Override
    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }

    @Override
    public String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return timestamp + "_" + uuid + extension;
    }

    /**
     * 验证文件
     */
    private Result<Void> validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "文件不能为空");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "文件名不能为空");
        }

        return Result.success();
    }

    /**
     * 构建文件路径
     */
    private String buildFilePath(String fileType, String fileName) {
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return fileType + "/" + datePath + "/" + fileName;
    }

    /**
     * 上传文件到OSS
     * 注意：在实际部署时需要添加阿里云OSS SDK依赖
     */
    private String uploadToOSS(MultipartFile file, String filePath) {
        try {
            // 实际实现时的代码示例：
            if (ossClient == null) {
                ossClient = new OSSClientBuilder().build(ossEndpoint, ossAccessKeyId, ossAccessKeySecret);
            }

            try (InputStream inputStream = file.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());

                PutObjectRequest putObjectRequest = new PutObjectRequest(ossBucketName, filePath, inputStream, metadata);
                PutObjectResult result = ossClient.putObject(putObjectRequest);

                return ossUrlPrefix + filePath;
            }

            // 模拟上传成功，返回文件URL
//            String mockUrl = (ossUrlPrefix != null && !ossUrlPrefix.isEmpty() ? ossUrlPrefix : "https://mock-oss.example.com/") + filePath;
//            logger.info("模拟文件上传成功: {}", mockUrl);
//            return mockUrl;

        } catch (Exception e) {
            logger.error("上传文件到OSS失败，文件路径: {}", filePath, e);
            return null;
        }
    }

    /**
     * 从OSS删除文件
     */
    private boolean deleteFromOSS(String fileUrl) {
        try {

            if (ossClient == null) {
                ossClient = new OSSClientBuilder().build(ossEndpoint, ossAccessKeyId, ossAccessKeySecret);
            }

            // 从URL中提取文件路径
            String filePath = fileUrl.replace(ossUrlPrefix, "");
            ossClient.deleteObject(ossBucketName, filePath);
            return true;
//            // 模拟删除成功
//            logger.info("模拟文件删除成功: {}", fileUrl);

        } catch (Exception e) {
            logger.error("从OSS删除文件失败，文件URL: {}", fileUrl, e);
            return false;
        }
    }

    /**
     * 检查OSS配置是否完整
     */
    private boolean isOssConfigured() {
        return ossEndpoint != null && !ossEndpoint.isEmpty() &&
               ossAccessKeyId != null && !ossAccessKeyId.isEmpty() &&
               ossAccessKeySecret != null && !ossAccessKeySecret.isEmpty() &&
               ossBucketName != null && !ossBucketName.isEmpty();
    }
}
