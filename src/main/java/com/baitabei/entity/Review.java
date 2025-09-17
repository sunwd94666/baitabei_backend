package com.baitabei.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 评审记录实体类
 * 
 * @author MiniMax Agent
 */
@Schema(description = "评审记录")
@TableName("evaluations")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "评审ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "项目ID")
    @TableField("project_id")
    private Long projectId;

    @Schema(description = "评审员ID")
    @TableField("evaluator_id")
    private Long reviewerId;

    @Schema(description = "评审轮次：1-初审，2-复审，3-终审")
    @TableField("round")
    private Integer round;

    @Schema(description = "标题")
    @TableField(exist = false)
    private String title;

    @Schema(description = "评审内容")
    @TableField("comments")
    private String content;

    @Schema(description = "创意性得分")
    @TableField("creativity_score")
    private BigDecimal creativityScore;

    @Schema(description = "实用性得分")
    @TableField("practicality_score")
    private BigDecimal practicalityScore;

    @Schema(description = "技术实现得分")
    @TableField("technology_score")
    private BigDecimal technologyScore;

    @Schema(description = "市场潜力得分")
    @TableField("market_score")
    private BigDecimal marketScore;

    @Schema(description = "文化内涵得分")
    @TableField("culture_score")
    private BigDecimal cultureScore;

    @Schema(description = "总分")
    @TableField("total_score")
    private BigDecimal totalScore;

    @Schema(description = "加权总分")
    @TableField("weighted_score")
    private BigDecimal weightedScore;

    @Schema(description = "改进建议")
    @TableField("suggestions")
    private String suggestions;

    @Schema(description = "状态：1-草稿，2-已提交")
    @TableField("status")
    private Integer status;

    @Schema(description = "评审类型")
    @TableField(exist = false)
    private String reviewType;

    @Schema(description = "提交时间")
    @TableField("submit_time")
    private LocalDateTime submitTime;

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

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getCreativityScore() {
        return creativityScore;
    }

    public void setCreativityScore(BigDecimal creativityScore) {
        this.creativityScore = creativityScore;
    }

    public BigDecimal getPracticalityScore() {
        return practicalityScore;
    }

    public void setPracticalityScore(BigDecimal practicalityScore) {
        this.practicalityScore = practicalityScore;
    }

    public BigDecimal getTechnologyScore() {
        return technologyScore;
    }

    public void setTechnologyScore(BigDecimal technologyScore) {
        this.technologyScore = technologyScore;
    }

    public BigDecimal getMarketScore() {
        return marketScore;
    }

    public void setMarketScore(BigDecimal marketScore) {
        this.marketScore = marketScore;
    }

    public BigDecimal getCultureScore() {
        return cultureScore;
    }

    public void setCultureScore(BigDecimal cultureScore) {
        this.cultureScore = cultureScore;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public BigDecimal getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(BigDecimal weightedScore) {
        this.weightedScore = weightedScore;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
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
        return "Review{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", reviewerId=" + reviewerId +
                ", round=" + round +
                ", totalScore=" + totalScore +
                ", status=" + status +
                ", submitTime=" + submitTime +
                '}';
    }
}