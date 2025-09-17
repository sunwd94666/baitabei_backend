package com.baitabei.dto;

import com.baitabei.security.UserPrincipal;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Schema(description = "项目提交请求")
public class ProjectSubmitRequest {

    @Schema(description = "项目信息", required = true)
    @JsonProperty("projectDto")
    @NotNull(message = "项目信息不能为空")
    @Valid
    private ProjectDto projectDto;

    @Schema(description = "用户主体信息", required = true)
    @JsonProperty("userPrincipal")
    @NotNull(message = "用户主体信息不能为空")
    @Valid
    private UserPrincipal userPrincipal;

    // Getters and Setters
    public ProjectDto getProjectDto() {
        return projectDto;
    }

    public void setProjectDto(ProjectDto projectDto) {
        this.projectDto = projectDto;
    }

    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }
}