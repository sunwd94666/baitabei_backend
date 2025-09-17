package com.baitabei.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 参赛项目实体类
 * 
 * @author MiniMax Agent
 */
@Schema(description = "参赛项目信息")
@TableName("projects")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "项目ID")
    @TableId(value = "id", type = IdType.AUTO)

    private Long id;

    @Schema(description = "项目编号")
    @TableField("project_code")
    private String projectCode;

    @Schema(description = "项目名称")
    @TableField("project_name")
    private String projectName;

    @Schema(description = "赛道ID")
    @TableField("track_id")
    private Long trackId;

    @Schema(description = "申报用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "项目描述")
    @TableField("description")
    private String description;

    @Schema(description = "详细描述")
    @TableField("detailed_description")
    private String detailedDescription;

    @Schema(description = "创新点")
    @TableField("innovation_points")
    private String innovationPoints;

    @Schema(description = "技术方案")
    @TableField("technical_solution")
    private String technicalSolution;

    @Schema(description = "市场分析")
    @TableField("market_analysis")
    private String marketAnalysis;

    @Schema(description = "团队信息")
    @TableField("team_info")
    private String teamInfo;

    @Schema(description = "项目封面图")
    @TableField("cover_image")
    private String coverImage;

    @Schema(description = "演示地址")
    @TableField("demo_url")
    private String demoUrl;

    @Schema(description = "视频地址")
    @TableField("video_url")
    private String videoUrl;

    @Schema(description = "状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "提交时间")
    @TableField("submit_time")
    private LocalDateTime submitTime;

    @Schema(description = "审核时间")
    @TableField("review_time")
    private LocalDateTime reviewTime;

    @Schema(description = "审核意见")
    @TableField("review_comment")
    private String reviewComment;

    @Schema(description = "最终得分")
    @TableField("final_score")
    private BigDecimal finalScore;

    @Schema(description = "排名")
    @TableField("ranking")
    private Integer ranking;

    @Schema(description = "获奖等级")
    @TableField("award_level")
    private String awardLevel;

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

    @Schema(description = "参赛信息数据")
    @TableField("track_json")
    private String trackJson;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getInnovationPoints() {
        return innovationPoints;
    }

    public void setInnovationPoints(String innovationPoints) {
        this.innovationPoints = innovationPoints;
    }

    public String getTechnicalSolution() {
        return technicalSolution;
    }

    public void setTechnicalSolution(String technicalSolution) {
        this.technicalSolution = technicalSolution;
    }

    public String getMarketAnalysis() {
        return marketAnalysis;
    }

    public void setMarketAnalysis(String marketAnalysis) {
        this.marketAnalysis = marketAnalysis;
    }

    public String getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(String teamInfo) {
        this.teamInfo = teamInfo;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getAwardLevel() {
        return awardLevel;
    }

    public void setAwardLevel(String awardLevel) {
        this.awardLevel = awardLevel;
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

    public String getTrackJson() {
        return trackJson;
    }

    public void setTrackJson(String trackJson) {
        this.trackJson = trackJson;
    }
}