package com.baitabei.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 赛道实体类
 * 
 * @author MiniMax Agent
 */
@Schema(description = "赛道信息")
@TableName("tracks")
public class Track implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "赛道ID")
    @TableId(value = "id", type = IdType.AUTO)

    private Long id;

    @Schema(description = "赛道编码")
    @TableField("track_code")
    private String trackCode;

    @Schema(description = "赛道名称")
    @TableField("track_name")
    private String trackName;

    @Schema(description = "赛道描述")
    @TableField("description")
    private String description;

    @Schema(description = "赛道封面图")
    @TableField("cover_image")
    private String coverImage;

    @Schema(description = "详细描述")
    @TableField("detailed_description")
    private String detailedDescription;

    @Schema(description = "参赛要求")
    @TableField("requirements")
    private String requirements;

    @Schema(description = "奖励设置")
    @TableField("rewards")
    private String rewards;

    @Schema(description = "排序")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "状态：0-关闭，1-开放")
    @TableField("status")
    private Integer status;

    @Schema(description = "开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    @Schema(description = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @Schema(description = "删除标志")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    // Constructors
    public Track() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", trackCode='" + trackCode + '\'' +
                ", trackName='" + trackName + '\'' +
                ", description='" + description + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", sortOrder=" + sortOrder +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", deleted=" + deleted +
                '}';
    }
}
