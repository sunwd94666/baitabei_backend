package com.baitabei.controller;

import com.baitabei.security.UserPrincipal;
import com.baitabei.service.UserService;
import com.baitabei.service.ProjectService;
import com.baitabei.service.ReviewService;
import com.baitabei.mapper.ReviewMapper;
import com.baitabei.common.result.Result;
import com.baitabei.common.result.ResultCode;
import com.baitabei.vo.UserEditVo;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 数据统计控制器
 */
@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * 获取概览统计数据
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverviewStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // 基础统计
            stats.put("totalUsers", userService.countUsers());
            stats.put("totalProjects", projectService.countProjects());
            stats.put("totalReviews", reviewMapper.countReviews());
            stats.put("activeUsers", userService.countActiveUsers());

            // 今日统计
            stats.put("todayNewUsers", userService.countTodayNewUsers());
            stats.put("todayNewProjects", projectService.countTodayNewProjects());

            // 在线统计
            stats.put("onlineUsers", userService.countOnlineUsers());

            // 提交统计
            stats.put("submittedProjects", projectService.countSubmittedProjects());
            stats.put("completedReviews", reviewMapper.countCompletedReviews());
            stats.put("pendingReviews", reviewMapper.countPendingReviews());

            logger.info("获取概览统计数据成功");
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取概览统计数据失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取用户统计数据
     */
    @GetMapping("/users")
    public Result<Map<String, Object>> getUserStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            long totalUsers = userService.countUsers();
            long activeUsers = userService.countActiveUsers();
            long todayNewUsers = userService.countTodayNewUsers();
            long onlineUsers = userService.countOnlineUsers();

            stats.put("totalUsers", totalUsers);
            stats.put("activeUsers", activeUsers);
            stats.put("newUsers", todayNewUsers);
            stats.put("onlineUsers", onlineUsers);

            // 用户增长趋势（模拟数据，实际项目中应该从数据库获取）
            Map<String, Integer> userGrowthTrend = new HashMap<>();
            LocalDate now = LocalDate.now();
            for (int i = 6; i >= 0; i--) {
                LocalDate date = now.minusDays(i);
                // 这里应该从数据库查询实际的每日新增用户数据
                userGrowthTrend.put(date.toString(), (int) (Math.random() * 20 + 5));
            }
            stats.put("userGrowthTrend", userGrowthTrend);

            // 用户状态分布
            Map<String, Long> userStatusDistribution = new HashMap<>();
            userStatusDistribution.put("active", activeUsers);
            userStatusDistribution.put("inactive", totalUsers - activeUsers);
            stats.put("userStatusDistribution", userStatusDistribution);

            logger.info("获取用户统计数据成功");
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取用户统计数据失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取项目统计数据
     */
    @GetMapping("/projects")
    public Result<Map<String, Object>> getProjectStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            long totalProjects = projectService.countProjects();
            long submittedProjects = projectService.countSubmittedProjects();
            long todayNewProjects = projectService.countTodayNewProjects();

            stats.put("totalProjects", totalProjects);
            stats.put("submittedProjects", submittedProjects);
            stats.put("todayNewProjects", todayNewProjects);
            stats.put("draftProjects", totalProjects - submittedProjects);

            // 按赛道统计项目数量
            List<Map<String, Object>> projectsByTrack = projectService.countProjectsByTrack();
            stats.put("projectsByTrack", projectsByTrack != null ? projectsByTrack : new ArrayList<>());

            // 按状态统计项目数量
            List<Map<String, Object>> projectsByStatus = projectService.countProjectsByStatus();
            stats.put("projectsByStatus", projectsByStatus != null ? projectsByStatus : new ArrayList<>());

            // 项目提交趋势（模拟数据）
            Map<String, Integer> submissionTrend = new HashMap<>();
            LocalDate now = LocalDate.now();
            for (int i = 6; i >= 0; i--) {
                LocalDate date = now.minusDays(i);
                submissionTrend.put(date.toString(), (int) (Math.random() * 15 + 3));
            }
            stats.put("submissionTrend", submissionTrend);

            logger.info("获取项目统计数据成功");
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取项目统计数据失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取评审统计数据
     */
    @GetMapping("/reviews")
    public Result<Map<String, Object>> getReviewStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            long totalReviews = reviewMapper.countReviews();
            long completedReviews = reviewMapper.countCompletedReviews();
            long pendingReviews = reviewMapper.countPendingReviews();
            Double avgScore = reviewMapper.getAverageReviewScore();

            stats.put("totalReviews", totalReviews);
            stats.put("completedReviews", completedReviews);
            stats.put("pendingReviews", pendingReviews);
            stats.put("avgReviewScore", avgScore != null ? avgScore : 0.0);

            // 完成率
            double completionRate = totalReviews > 0 ? (double) completedReviews / totalReviews * 100 : 0.0;
            stats.put("completionRate", Math.round(completionRate * 100.0) / 100.0);

            // 评审趋势（模拟数据）
            Map<String, Integer> reviewTrend = new HashMap<>();
            LocalDate now = LocalDate.now();
            for (int i = 6; i >= 0; i--) {
                LocalDate date = now.minusDays(i);
                reviewTrend.put(date.toString(), (int) (Math.random() * 10 + 2));
            }
            stats.put("reviewTrend", reviewTrend);

            logger.info("获取评审统计数据成功");
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取评审统计数据失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取赛道统计数据
     */
    @GetMapping("/tracks")
    public Result<Map<String, Object>> getTrackStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // 按赛道统计项目数量
            List<Map<String, Object>> projectsByTrack = projectService.countProjectsByTrack();
            stats.put("projectsByTrack", projectsByTrack != null ? projectsByTrack : new ArrayList<>());

            // 赛道参与度排名
            if (projectsByTrack != null && !projectsByTrack.isEmpty()) {
                stats.put("mostPopularTrack", projectsByTrack.get(0));
            }

            // 赛道总数（模拟数据）
            stats.put("totalTracks", 8);
            stats.put("activeTracks", 8);

            logger.info("获取赛道统计数据成功");
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取赛道统计数据失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/tracks")
    @Operation(summary = "按赛道统计项目数量")
    public Result<List<Map<String, Object>>> countProjectsByTrack(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Map<String, Object>> list = projectService.getProjectsCountByTrack(userPrincipal);
        return Result.success(list);
    }

    @GetMapping("/countByRoles")
    @Operation(summary = "按角色统计用户数",
            description = "可传roleId或roleCode精确查询，都不传则返回全部角色")
    public Result<List<Map<String,Object>>> countUsersByRole(
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) String roleCode) {
        List<Map<String,Object>> list = userService.countUsersByRole(roleId, roleCode);
        return Result.success(list);
    }

    /**
     * 获取时间范围统计数据
     */
    @GetMapping("/timerange")
    public Result<Map<String, Object>> getTimeRangeStatistics(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "day") String granularity) {
        try {
            Map<String, Object> stats = new HashMap<>();

            logger.info("获取时间范围统计，开始日期: {}, 结束日期: {}, 粒度: {}", startDate, endDate, granularity);

            // 在实际项目中，这里应该根据时间范围和粒度查询数据库
            // 目前返回模拟数据

            Map<String, Integer> userStats = new HashMap<>();
            Map<String, Integer> projectStats = new HashMap<>();
            Map<String, Integer> reviewStats = new HashMap<>();

            // 生成时间序列数据
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            LocalDate current = start;

            while (!current.isAfter(end)) {
                String dateStr = current.toString();
                userStats.put(dateStr, (int) (Math.random() * 20 + 5));
                projectStats.put(dateStr, (int) (Math.random() * 15 + 3));
                reviewStats.put(dateStr, (int) (Math.random() * 10 + 2));

                // 根据粒度递增日期
                switch (granularity.toLowerCase()) {
                    case "day":
                        current = current.plusDays(1);
                        break;
                    case "week":
                        current = current.plusWeeks(1);
                        break;
                    case "month":
                        current = current.plusMonths(1);
                        break;
                    default:
                        current = current.plusDays(1);
                }
            }

            stats.put("userStats", userStats);
            stats.put("projectStats", projectStats);
            stats.put("reviewStats", reviewStats);
            stats.put("granularity", granularity);
            Map<String, Object> dateRange = new HashMap<>();
            dateRange.put("start", startDate);
            dateRange.put("end", endDate);
            stats.put("dateRange", Collections.unmodifiableMap(dateRange));
//            stats.put("dateRange", Map.of("start", startDate, "end", endDate));

            logger.info("获取时间范围统计数据成功");
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取时间范围统计数据失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 导出统计报表
     */
    @GetMapping("/export")
    public Result<String> exportStatisticsReport(
            @RequestParam String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            logger.info("导出统计报表，类型: {}, 开始日期: {}, 结束日期: {}", type, startDate, endDate);

            // 在实际项目中，这里应该生成Excel或PDF报表文件
            // 目前返回模拟的下载链接

            String fileName = String.format("%s_report_%s.xlsx", type, LocalDateTime.now().toString().replace(":", "-"));
            String downloadUrl = "/downloads/" + fileName;

            logger.info("统计报表导出成功，文件: {}", fileName);
            return Result.success(downloadUrl);
        } catch (Exception e) {
            logger.error("导出统计报表失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }
}
