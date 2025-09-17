package com.baitabei.controller;

import com.baitabei.service.ReviewService;
import com.baitabei.dto.ReviewDto;
import com.baitabei.vo.ReviewVo;
import com.baitabei.common.result.Result;
import com.baitabei.common.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 评审管理控制器
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * 创建评审
     */
    @PostMapping
    public Result<Long> createReview(@RequestBody ReviewDto reviewDto) {
        Long reviewId = reviewService.createReview(reviewDto);
        return Result.success(reviewId);
    }
    
    /**
     * 更新评审
     */
    @PutMapping("/{reviewId}")
    public Result<Void> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDto reviewDto) {
        reviewService.updateReview(reviewId, reviewDto);
        return Result.success();
    }
    
    /**
     * 删除评审
     */
    @DeleteMapping("/{reviewId}")
    public Result<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return Result.success();
    }
    
    /**
     * 获取评审详情
     */
    @GetMapping("/{reviewId}")
    public Result<ReviewVo> getReviewById(@PathVariable Long reviewId) {
        ReviewVo reviewVo = reviewService.getReviewById(reviewId);
        return Result.success(reviewVo);
    }
    
    /**
     * 分页查询评审列表
     */
    @GetMapping
    public Result<PageResult<ReviewVo>> getReviewList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<ReviewVo> result = reviewService.getReviewList(keyword, page, size);
        return Result.success(result);
    }
    
    /**
     * 提交评审结果
     */
    @PostMapping("/{reviewId}/result")
    public Result<Void> submitReviewResult(
            @PathVariable Long reviewId,
            @RequestParam String result,
            @RequestParam String comment) {
        reviewService.submitReviewResult(reviewId, result, comment);
        return Result.success();
    }
    
    /**
     * 获取项目的所有评审
     */
    @GetMapping("/project/{projectId}")
    public Result<PageResult<ReviewVo>> getReviewsByProjectId(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<ReviewVo> result = reviewService.getReviewsByProjectId(projectId, page, size);
        return Result.success(result);
    }
}
