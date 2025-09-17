package com.baitabei.service;

import com.baitabei.security.UserPrincipal;
import com.baitabei.vo.UserEditVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baitabei.common.result.Result;
import com.baitabei.dto.ProjectDto;
import com.baitabei.entity.Project;
import com.baitabei.vo.ProjectVo;

import java.util.List;
import java.util.Map;

/**
 * 项目服务接口
 * 
 * @author MiniMax Agent
 */
public interface ProjectService {

    /**
     * 创建项目
     * @param projectDto 项目信息
     * @param userId 用户ID
     * @return 创建结果
     */
    Result<Long> createProject(ProjectDto projectDto, Long userId);

    /**
     * 更新项目
     * @param projectDto 项目信息
     * @param userId 用户ID
     * @return 更新结果
     */
    Result<Void> updateProject(ProjectDto projectDto, Long userId);

    /**
     * 删除项目
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 删除结果
     */
    Result<Void> deleteProject(Long projectId, Long userId);

    /**
     * 根据ID获取项目
     * @param projectId 项目ID
     * @return 项目信息
     */
    Result<ProjectVo> getProjectById(Long projectId);

    /**
     * 分页查询项目列表
     * @param projectDto 查询条件
     * @return 项目列表
     */
    Result<IPage<ProjectVo>> getProjectPage(int pageNum, int pageSize,ProjectDto projectDto);

    /**
     * 获取用户项目列表
     * @param userId 用户ID
     * @return 项目列表
     */
    Result<List<ProjectVo>> getUserProjects(Long userId);

    /**
     * 提交项目
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 提交结果
     */
    Result<Void> submitProject1(Long projectId, Long userId);
    Result<Void> submitProject(ProjectDto projectDto,UserPrincipal userPrincipal);

    /**
     * 撤回项目
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 撤回结果
     */
    Result<Void> withdrawProject(Long projectId, Long userId);

    /**
     * 审核项目
     * @param projectId 项目ID
     * @param status 审核状态
     * @param reviewComment 审核意见
     * @return 审核结果
     */
    Result<Void> reviewProject(Long projectId, Integer status, String reviewComment);

    /**
     * 获取项目排名
     * @param trackId 赛道ID
     * @param limit 数量限制
     * @return 排名列表
     */
    Result<List<ProjectVo>> getProjectRankings(Long trackId, Integer limit);

    /**
     * 统计项目数量
     * @return 项目数量
     */
    long countProjects();

    /**
     * 统计已提交项目数量
     * @return 已提交项目数量
     */
    long countSubmittedProjects();

    /**
     * 统计今日新增项目数量
     * @return 今日新增项目数量
     */
    long countTodayNewProjects();

    /**
     * 按赛道统计项目数量
     * @return 统计结果
     */
    List<Map<String, Object>> countProjectsByTrack();

    /**
     * 按userid取赛道统计项目数量
     * @return 统计结果
     */
    List<Map<String, Object>> getProjectsCountByTrack(UserPrincipal  userEditVo);

    /**
     * 按状态统计项目数量
     * @return 统计结果
     */
    List<Map<String, Object>> countProjectsByStatus();
}
