package com.baitabei.mapper;

import com.baitabei.entity.Review;
import com.baitabei.vo.ReviewVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评审数据访问层
 */
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    /**
     * 分页查询评审列表
     */
    IPage<ReviewVo> selectReviewPage(Page<ReviewVo> page, @Param("keyword") String keyword);

    /**
     * 获取项目的所有评审
     */
    IPage<ReviewVo> selectReviewsByProjectId(Page<ReviewVo> page, @Param("projectId") Long projectId);

    /**
     * 获取评审详情
     */
    ReviewVo selectReviewVoById(@Param("reviewId") Long reviewId);

    /**
     * 统计评审数量
     */
    long countReviews();

    /**
     * 统计已完成评审数量
     */
    long countCompletedReviews();

    /**
     * 统计待审核评审数量
     */
    long countPendingReviews();

    /**
     * 计算平均评审分数
     */
    Double getAverageReviewScore();
}
