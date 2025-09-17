package com.baitabei.controller;

import com.baitabei.dto.ProjectSubmitRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baitabei.common.result.Result;
import com.baitabei.dto.ProjectDto;
import com.baitabei.security.UserPrincipal;
import com.baitabei.service.ProjectService;
import com.baitabei.vo.ProjectVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 项目控制器
 * 
 * @author MiniMax Agent
 */
@Tag(name = "项目管理", description = "项目管理")
@RestController
@RequestMapping("/api/project")
@Validated
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "创建项目")
    @PostMapping
    public Result<Long> createProject(@Valid @RequestBody ProjectDto projectDto,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return projectService.createProject(projectDto, userPrincipal.getId());
    }

    @Operation(summary = "更新项目")
    @PostMapping("/update")
    public Result<Void> updateProject(@Valid @RequestBody ProjectDto projectDto,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return projectService.updateProject(projectDto, userPrincipal.getId());
    }

    @Operation(summary = "删除项目")
    @DeleteMapping("/delete/{projectId}")
    public Result<Void> deleteProject(@PathVariable Long projectId,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return projectService.deleteProject(projectId, userPrincipal.getId());
    }

    @Operation(summary = "获取项目详情")
    @GetMapping("/detail/{projectId}")
    public Result<ProjectVo> getProjectById(@PathVariable Long projectId) {
        return projectService.getProjectById(projectId);
    }

    @Operation(summary = "分页查询项目列表")
    @GetMapping("/page")
    public Result<IPage<ProjectVo>> getProjectPage(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize,ProjectDto projectDto) {
        return projectService.getProjectPage(pageNum,pageSize,projectDto);
    }

    @Operation(summary = "获取当前用户项目列表")
    @GetMapping("/my")
    public Result<List<ProjectVo>> getMyProjects(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return projectService.getUserProjects(userPrincipal.getId());
    }

//    @Operation(summary = "提交项目")
//    @PostMapping("/{projectId}/submit")
//    public Result<Void> submitProject1(@PathVariable Long projectId,
//                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
//        return projectService.submitProject1(projectId, userPrincipal.getId());
//    }

    @Operation(summary = "提交项目")
    @PostMapping("/submit")
    public Result<Void> submitProject(@RequestBody ProjectDto projectDto,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return projectService.submitProject(projectDto, userPrincipal);
    }

    @Operation(summary = "撤回项目")
    @PostMapping("/{projectId}/withdraw")
    public Result<Void> withdrawProject(@PathVariable Long projectId,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return projectService.withdrawProject(projectId, userPrincipal.getId());
    }

    @Operation(summary = "审核项目")
    @PostMapping("/{projectId}/review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXPERT')")
    public Result<Void> reviewProject(@PathVariable Long projectId,
                                     @RequestParam Integer status,
                                     @RequestParam(required = false) String reviewComment) {
        return projectService.reviewProject(projectId, status, reviewComment);
    }

    @Operation(summary = "获取项目排名")
    @GetMapping("/rankings")
    public Result<List<ProjectVo>> getProjectRankings(@RequestParam(required = false) Long trackId,
                                                     @RequestParam(defaultValue = "10") Integer limit) {
        return projectService.getProjectRankings(trackId, limit);
    }

    @Operation(summary = "统计项目数量")
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> countProjects() {
        long count = projectService.countProjects();
        return Result.success(count);
    }

    @Operation(summary = "统计已提交项目数量")
    @GetMapping("/count/submitted")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> countSubmittedProjects() {
        long count = projectService.countSubmittedProjects();
        return Result.success(count);
    }

    @Operation(summary = "统计今日新增项目数量")
    @GetMapping("/count/today-new")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> countTodayNewProjects() {
        long count = projectService.countTodayNewProjects();
        return Result.success(count);
    }

    @Operation(summary = "按赛道统计项目数量")
    @GetMapping("/count/by-track")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<Map<String, Object>>> countProjectsByTrack() {
        List<Map<String, Object>> result = projectService.countProjectsByTrack();
        return Result.success(result);
    }

    @Operation(summary = "按状态统计项目数量")
    @GetMapping("/count/by-status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<Map<String, Object>>> countProjectsByStatus() {
        List<Map<String, Object>> result = projectService.countProjectsByStatus();
        return Result.success(result);
    }
}
