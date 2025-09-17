package com.baitabei.vo;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目视图对象
 * 
 * @author MiniMax Agent
 */
@Schema(description = "项目信息视图")
public class ProjectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "项目ID")
    private Long id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "赛道ID")
    private Long trackId;

    @Schema(description = "赛道编码")
    private String trackCode;

    @Schema(description = "赛道名称")
    private String trackName;

    @Schema(description = "申报用户ID")
    private Long userId;

    @Schema(description = "申报用户名")
    private String username;

    @Schema(description = "申报人真实姓名")
    private String realName;

    @Schema(description = "申报人头像")
    private String avatar;

    @Schema(description = "申报人所属机构")
    private String institution;

    @Schema(description = "项目描述")
    private String description;

    @Schema(description = "详细描述")
    private String detailedDescription;

    @Schema(description = "创新点")
    private String innovationPoints;

    @Schema(description = "技术方案")
    private String technicalSolution;

    @Schema(description = "市场分析")
    private String marketAnalysis;

    @Schema(description = "团队信息")
    private String teamInfo;

    @Schema(description = "项目封面图")
    private String coverImage;

    @Schema(description = "演示地址")
    private String demoUrl;

    @Schema(description = "视频地址")
    private String videoUrl;

    @Schema(description = "状态：0-草稿，1-已提交，2-审核中，3-审核通过，4-审核不通过")
    private Integer status;

    @Schema(description = "状态描述")
    private String statusName;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "审核时间")
    private LocalDateTime reviewTime;

    @Schema(description = "审核意见")
    private String reviewComment;

    @Schema(description = "最终得分")
    private BigDecimal finalScore;

    @Schema(description = "排名")
    private Integer ranking;

    @Schema(description = "获奖等级")
    private String awardLevel;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    @Schema(description = "项目文件数量")
    private Integer fileCount;

    @Schema(description = "是否可编辑")
    private Boolean editable;

    @Schema(description = "参赛信息数据")
    private JsonNode trackJson;

    // 构造函数
    public ProjectVo() {}

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
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

    public JsonNode getTrackJson() {
        return trackJson;
    }

    public void setTrackJson(JsonNode trackJson) {
        this.trackJson = trackJson;
    }

    public void setStatus(Integer status) {
        this.status = status;
        // 设置状态描述
        if (status != null) {
            switch (status) {
                case 0:
                    this.statusName = "草稿";
                    break;
                case 1:
                    this.statusName = "已提交";
                    break;
                case 2:
                    this.statusName = "审核中";
                    break;
                case 3:
                    this.statusName = "审核通过";
                    break;
                case 4:
                    this.statusName = "审核不通过";
                    break;
                default:
                    this.statusName = "未知";
                    break;
            }
        }
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    @Override
    public String toString() {
        return "ProjectVo{" +
                "id=" + id +
                ", projectCode='" + projectCode + '\'' +
                ", projectName='" + projectName + '\'' +
                ", trackName='" + trackName + '\'' +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", finalScore=" + finalScore +
                ", ranking=" + ranking +
                ", createdTime=" + createdTime +
                '}';
    }
}