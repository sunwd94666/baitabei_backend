package com.baitabei.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 评审数据传输对象
 */
public class ReviewDto {
    
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
    
    @NotNull(message = "评审员ID不能为空")
    private Long reviewerId;
    
    @Size(max = 500, message = "评审标题长度不能超过500字符")
    private String title;
    
    @Size(max = 2000, message = "评审内容长度不能超过2000字符")
    private String content;
    
    private Integer score;
    
    private String status;
    
    private String reviewType;
    
    // Getters and Setters
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
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReviewType() {
        return reviewType;
    }
    
    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }
}
