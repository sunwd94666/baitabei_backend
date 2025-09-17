package com.baitabei.service;

import com.baitabei.dto.ReviewDto;
import com.baitabei.vo.ReviewVo;
import com.baitabei.common.result.PageResult;

/**
 * 评审管理服务接口
 */
public interface ReviewService {
    
    /**
     * 创建评审
     */
    Long createReview(ReviewDto reviewDto);
    
    /**
     * 更新评审
     */
    void updateReview(Long reviewId, ReviewDto reviewDto);
    
    /**
     * 删除评审
     */
    void deleteReview(Long reviewId);
    
    /**
     * 获取评审详情
     */
    ReviewVo getReviewById(Long reviewId);
    
    /**
     * 分页查询评审列表
     */
    PageResult<ReviewVo> getReviewList(String keyword, Integer page, Integer size);
    
    /**
     * 提交评审结果
     */
    void submitReviewResult(Long reviewId, String result, String comment);
    
    /**
     * 获取项目的所有评审
     */
    PageResult<ReviewVo> getReviewsByProjectId(Long projectId, Integer page, Integer size);
}
