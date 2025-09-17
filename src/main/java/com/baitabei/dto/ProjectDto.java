package com.baitabei.dto;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 项目DTO
 * 
 * @author MiniMax Agent
 */
@Schema(description = "项目参数")
public class ProjectDto {

    @Schema(description = "项目ID")
    private Long id;

    @Schema(description = "项目名称")
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 200, message = "项目名称长度不能超过200个字符")
    private String projectName;

    @Schema(description = "赛道ID")
    @NotNull(message = "赛道ID不能为空")
    private Long trackId;

    @Schema(description = "项目状态")
    private Integer status;

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

    @Schema(description = "参赛信息数据")
    private String trackJson;

    @Schema(description = "更新时间")
    private DateTime updatedTime;

    public DateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(DateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    // Constructors
    public ProjectDto() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getTrackJson() {
        return trackJson;
    }

    public void setTrackJson(String trackJson) {
        this.trackJson = trackJson;
    }

    @Override
    public String toString() {
        return "ProjectDto{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", trackId=" + trackId +
                ", description='" + description + '\'' +
                '}';
    }
}
