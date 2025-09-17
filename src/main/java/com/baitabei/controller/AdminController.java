package com.baitabei.controller;

import com.baitabei.service.UserService;
import com.baitabei.service.ProjectService;
import com.baitabei.service.ReviewService;
import com.baitabei.mapper.ReviewMapper;
import com.baitabei.common.result.Result;
import com.baitabei.common.result.ResultCode;
import com.baitabei.entity.User;
import com.baitabei.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * 后台管理控制器
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * 获取系统概览数据
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardData() {
        try {
            Map<String, Object> data = new HashMap<>();

            // 用户统计
            long totalUsers = userService.countUsers();
            long activeUsers = userService.countActiveUsers();
            long todayNewUsers = userService.countTodayNewUsers();
            long onlineUsers = userService.countOnlineUsers();

            // 项目统计
            long totalProjects = projectService.countProjects();
            long submittedProjects = projectService.countSubmittedProjects();
            long todayNewProjects = projectService.countTodayNewProjects();

            // 评审统计
            long totalReviews = reviewMapper.countReviews();
            long completedReviews = reviewMapper.countCompletedReviews();
            long pendingReviews = reviewMapper.countPendingReviews();
            Double avgReviewScore = reviewMapper.getAverageReviewScore();

            // 组织数据
            data.put("totalUsers", totalUsers);
            data.put("activeUsers", activeUsers);
            data.put("todayNewUsers", todayNewUsers);
            data.put("onlineUsers", onlineUsers);

            data.put("totalProjects", totalProjects);
            data.put("submittedProjects", submittedProjects);
            data.put("todayNewProjects", todayNewProjects);
            data.put("pendingProjects", totalProjects - submittedProjects);

            data.put("totalReviews", totalReviews);
            data.put("completedReviews", completedReviews);
            data.put("pendingReviews", pendingReviews);
            data.put("avgReviewScore", avgReviewScore != null ? avgReviewScore : 0.0);

            // 系统状态
            data.put("systemStatus", "正常");
            data.put("lastUpdateTime", LocalDateTime.now());

            logger.info("获取系统概览数据成功");
            return Result.success(data);
        } catch (Exception e) {
            logger.error("获取系统概览数据失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> adminLogin(@RequestBody Map<String, String> loginData) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            logger.info("管理员登录尝试，用户名: {}", username);

            // 简单的管理员验证（在实际项目中应该使用更安全的验证方式）
            if ("admin".equals(username) && "admin123".equals(password)) {
                Map<String, Object> result = new HashMap<>();
                result.put("token", "admin_token_" + System.currentTimeMillis());

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", 1L);
                userInfo.put("username", "admin");
                userInfo.put("realName", "系统管理员");
                userInfo.put("role", "admin");
                userInfo.put("permissions", new String[]{"admin:*"});

                result.put("userInfo", userInfo);
                result.put("loginTime", LocalDateTime.now());

                logger.info("管理员登录成功，用户名: {}", username);
                return Result.success(result);
            } else {
                logger.warn("管理员登录失败，用户名或密码错误: {}", username);
                return Result.error(ResultCode.LOGIN_FAILED);
            }
        } catch (Exception e) {
            logger.error("管理员登录异常", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取系统配置
     */
    @GetMapping("/config")
    public Result<Map<String, Object>> getSystemConfig() {
        try {
            Map<String, Object> config = new HashMap<>();

            // 基础配置
            config.put("siteName", "白塔杯文化创意大赛");
            config.put("siteDescription", "2025年白塔杯文化创意大赛管理系统");
            config.put("version", "1.0.0");

            // 功能配置
            config.put("registrationEnabled", true);
            config.put("submissionEnabled", true);
            config.put("reviewEnabled", true);

            // 限制配置
            config.put("maxProjectsPerUser", 5);
            config.put("maxFileSize", 10 * 1024 * 1024); // 10MB
            config.put("allowedFileTypes", new String[]{".pdf", ".doc", ".docx", ".jpg", ".png"});

            // 评审配置
            config.put("reviewRounds", 3);
            config.put("maxReviewersPerProject", 5);

            logger.info("获取系统配置成功");
            return Result.success(config);
        } catch (Exception e) {
            logger.error("获取系统配置失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新系统配置
     */
    @PutMapping("/config")
    public Result<Void> updateSystemConfig(@RequestBody Map<String, Object> config) {
        try {
            logger.info("更新系统配置: {}", config);

            // 在实际项目中，这里应该将配置保存到数据库的系统配置表中
            // 目前只是记录日志表示更新成功

            logger.info("系统配置更新成功");
            return Result.success();
        } catch (Exception e) {
            logger.error("更新系统配置失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 用户管理操作
     */
    @PutMapping("/users/{userId}/status")
    public Result<Void> updateUserStatus(@PathVariable Long userId, @RequestParam String status) {
        try {
            logger.info("更新用户状态，用户ID: {}, 状态: {}", userId, status);

            // 状态转换
            Integer statusCode;
            switch (status.toLowerCase()) {
                case "active":
                case "1":
                    statusCode = 1;
                    break;
                case "disabled":
                case "0":
                    statusCode = 0;
                    break;
                case "pending":
                case "2":
                    statusCode = 2;
                    break;
                default:
                    return Result.error(ResultCode.BAD_REQUEST.getCode(), "无效的用户状态");
            }

            com.baitabei.common.result.Result<Void> result = userService.updateUserStatus(userId, statusCode);

            if (result.isSuccess()) {
                logger.info("用户状态更新成功，用户ID: {}, 新状态: {}", userId, statusCode);
                return Result.success();
            } else {
                return Result.error(result.getCode(), result.getMessage());
            }
        } catch (Exception e) {
            logger.error("更新用户状态失败，用户ID: {}, 状态: {}", userId, status, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 项目审核
     */
    @PutMapping("/projects/{projectId}/audit")
    public Result<Void> auditProject(@PathVariable Long projectId, @RequestParam String status) {
        try {
            logger.info("项目审核，项目ID: {}, 状态: {}", projectId, status);

            // 状态转换
            Integer statusCode;
            String comment = "管理员审核";

            switch (status.toLowerCase()) {
                case "approved":
                case "pass":
                    statusCode = 4; // 初审通过
                    comment = "审核通过";
                    break;
                case "rejected":
                case "fail":
                    statusCode = 5; // 初审不通过
                    comment = "审核不通过";
                    break;
                case "reviewing":
                    statusCode = 3; // 初审中
                    comment = "审核中";
                    break;
                default:
                    return Result.error(ResultCode.BAD_REQUEST.getCode(), "无效的审核状态");
            }

            com.baitabei.common.result.Result<Void> result = projectService.reviewProject(projectId, statusCode, comment);

            if (result.isSuccess()) {
                logger.info("项目审核成功，项目ID: {}, 新状态: {}", projectId, statusCode);
                return Result.success();
            } else {
                return Result.error(result.getCode(), result.getMessage());
            }
        } catch (Exception e) {
            logger.error("项目审核失败，项目ID: {}, 状态: {}", projectId, status, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }
}
