package com.baitabei.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 项目文件实体类
 * 
 * @author MiniMax Agent
 */
@Schema(description = "项目文件")
@TableName("project_files")
public class ProjectFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "文件ID")
    @TableId(value = "id", type = IdType.AUTO)

    private Long id;

    @Schema(description = "项目ID")
    @TableField("project_id")
    private Long projectId;

    @Schema(description = "文件名")
    @TableField("file_name")
    private String fileName;

    @Schema(description = "文件类型")
    @TableField("file_type")
    private String fileType;

    @Schema(description = "文件大小")
    @TableField("file_size")
    private Long fileSize;

    @Schema(description = "文件URL")
    @TableField("file_url")
    private String fileUrl;

    @Schema(description = "文件路径")
    @TableField("file_path")
    private String filePath;

    @Schema(description = "文件分类")
    @TableField("category")
    private String category;

    @Schema(description = "文件描述")
    @TableField("description")
    private String description;

    @Schema(description = "上传用户ID")
    @TableField("upload_user_id")
    private Long uploadUserId;

    @Schema(description = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @Schema(description = "删除标志")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    // Constructors
    public ProjectFile() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(Long uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "ProjectFile{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", fileUrl='" + fileUrl + '\'' +
                ", category='" + category + '\'' +
                ", uploadUserId=" + uploadUserId +
                ", createdTime=" + createdTime +
                '}';
    }
}
