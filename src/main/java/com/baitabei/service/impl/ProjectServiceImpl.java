package com.baitabei.service.impl;

import cn.hutool.core.date.DateTime;
import com.baitabei.mapper.UserTracksMapper;
import com.baitabei.security.UserPrincipal;
import com.baitabei.util.JsonUtils;
import com.baitabei.vo.UserEditVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baitabei.common.result.Result;
import com.baitabei.common.result.ResultCode;
import com.baitabei.constant.AppConstants;
import com.baitabei.dto.ProjectDto;
import com.baitabei.entity.Project;
import com.baitabei.entity.Track;
import com.baitabei.entity.User;
import com.baitabei.enums.ProjectStatus;
import com.baitabei.exception.BusinessException;
import com.baitabei.mapper.ProjectMapper;
import com.baitabei.mapper.UserMapper;
import com.baitabei.service.ProjectService;
import com.baitabei.util.StringUtil;
import com.baitabei.vo.ProjectVo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 项目服务实现类
 * 
 * @author MiniMax Agent
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private UserTracksMapper userTracksMapper;
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private UserMapper userMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    @Transactional
    public Result<Long> createProject(ProjectDto projectDto, Long userId) {
        try {
            // 验证用户是否存在
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }

            // 验证必要的字段
//            if (!StringUtils.hasText(projectDto.getProjectName())) {
//                return Result.error(ResultCode.BAD_REQUEST.getCode(), "项目名称不能为空");
//            }
            if (projectDto.getTrackId() == null) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "赛道ID不能为空");
            }
            String projectName = projectDto.getProjectName();

            // 创建项目实体
            Project project = new Project();
            BeanUtils.copyProperties(projectDto, project);
            project.setProjectName(projectName);
            project.setUserId(userId);
            project.setProjectCode(generateProjectCode());
//            project.setStatus(ProjectStatus.DRAFT.getCode());
            project.setCreatedTime(LocalDateTime.now());
            project.setUpdatedTime(LocalDateTime.now());
            project.setTrackJson(projectDto.getTrackJson().toString());

            //
            project.setStatus(ProjectStatus.SUBMITTED.getCode());
            project.setUpdatedTime(LocalDateTime.now());

            // 保存项目
            int result = projectMapper.insert(project);
            if (result > 0) {
                logger.info("创建项目成功，项目ID: {}, 用户ID: {}", project.getId(), userId);
                return Result.success(project.getId());
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("创建项目失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public Result<Void> updateProject(ProjectDto projectDto, Long userId) {
        try {
            if (projectDto.getId() == null) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "项目ID不能为空");
            }

            // 验证项目是否存在
            Project existingProject = projectMapper.selectById(projectDto.getId());
            if (existingProject == null) {
                return Result.error(ResultCode.PROJECT_NOT_FOUND);
            }

            // 验证是否是项目所有者
            if (!existingProject.getUserId().equals(userId)) {
                return Result.error(ResultCode.PROJECT_NOT_OWNER);
            }

            // 验证项目状态是否可编辑
            //无需验证，前端会把按钮置灰
//            if (!isEditable(existingProject.getStatus())) {
//                return Result.error(ResultCode.PROJECT_STATUS_ERROR.getCode(), "当前状态不允许编辑");
//            }

            // 更新项目信息
            Project project = new Project();
            BeanUtils.copyProperties(projectDto, project);
            project.setUpdatedTime(LocalDateTime.now());
            
            int result = projectMapper.updateById(project);
            if (result > 0) {
                logger.info("更新项目成功，项目ID: {}, 用户ID: {}", project.getId(), userId);
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("更新项目失败，项目ID: {}, 用户ID: {}", projectDto.getId(), userId, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public Result<Void> deleteProject(Long projectId, Long userId) {
        try {
            // 验证项目是否存在
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return Result.error(ResultCode.PROJECT_NOT_FOUND);
            }

            // 验证是否是项目所有者
//            if (!project.getUserId().equals(userId)) {
//                return Result.error(ResultCode.PROJECT_NOT_OWNER);
//            }

            // 验证项目状态是否可删除（只有草稿状态可以删除）
//            if (!ProjectStatus.DRAFT.getCode().equals(project.getStatus())) {
//                return Result.error(ResultCode.PROJECT_STATUS_ERROR.getCode(), "只有草稿状态的项目可以删除");
//            }

            int result = projectMapper.deleteById(projectId);
            if (result > 0) {
                logger.info("删除项目成功，项目ID: {}, 用户ID: {}", projectId, userId);
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("删除项目失败，项目ID: {}, 用户ID: {}", projectId, userId, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<ProjectVo> getProjectById(Long projectId) {
        try {
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return Result.error(ResultCode.PROJECT_NOT_FOUND);
            }

            ProjectVo projectVo = convertToVo(project);
            return Result.success(projectVo);
        } catch (Exception e) {
            logger.error("获取项目详情失败，项目ID: {}", projectId, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

//    @Override
//    public Result<IPage<ProjectVo>> getProjectPage(ProjectDto projectDto) {
//        try {
//            // 设置分页参数
//            Integer pageNum = projectDto.getId() != null ? projectDto.getId().intValue() : AppConstants.DEFAULT_PAGE_NUM;
//            Integer pageSize = AppConstants.DEFAULT_PAGE_SIZE;
//
//            if (pageSize > AppConstants.MAX_PAGE_SIZE) {
//                pageSize = AppConstants.MAX_PAGE_SIZE;
//            }
//
//            Page<ProjectVo> page = new Page<>(pageNum, pageSize);
//            IPage<ProjectVo> projectPage = projectMapper.selectProjectPage(page, projectDto);
//
//            return Result.success(projectPage);
//        } catch (Exception e) {
//            logger.error("分页查询项目列表失败", e);
//            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
//        }
//    }

    @Override
    public Result<IPage<ProjectVo>> getProjectPage(int pageNum, int pageSize,ProjectDto projectDto) {
        try {
            // 1. 分页参数优先取 projectDto 中的页码、页大小；为空再拿全局默认值
//            pageNum = pageNum != null ? pageNum: AppConstants.DEFAULT_PAGE_NUM;
//            pageSize = pageSize != null ? pageNum : AppConstants.DEFAULT_PAGE_SIZE;

            // 2. 防恶意大页码
            if (pageSize > AppConstants.MAX_PAGE_SIZE) {
                pageSize = AppConstants.MAX_PAGE_SIZE;
            }

            // 3. 构建分页对象
            Page<ProjectVo> page = new Page<>(pageNum, pageSize);

            // 4. 查询
            IPage<ProjectVo> projectPage = projectMapper.selectProjectPage(page, projectDto);

            // 5. 返回
            return Result.success(projectPage);
        } catch (Exception e) {
            logger.error("分页查询项目列表失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<List<ProjectVo>> getUserProjects(Long userId) {
        try {
            List<Project> projects = projectMapper.findProjectsByUserId(userId);
            List<ProjectVo> projectVos = projects.stream()
                    .map(this::convertToVo)
                    .collect(java.util.stream.Collectors.toList());
            
            return Result.success(projectVos);
        } catch (Exception e) {
            logger.error("获取用户项目列表失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<Void> submitProject1(Long projectId, Long userId) {
        return null;
    }


    @Override
    @Transactional
    public Result<Void> submitProject(ProjectDto projectDto,UserPrincipal userPrincipal) {
//        projectDto.setStatus(ProjectStatus.SUBMITTED.getCode());
//        projectDto.setSubmitAable(Boolean.TRUE);
//        projectDto.setUpdatedTime(DateTime.now());

        String trackJson = projectDto.getTrackJson();
        ObjectNode root = StringUtils.hasText(trackJson)
                ? (ObjectNode) JsonUtils.fromJson(trackJson)
                : JsonUtils.createObjectNode();
        projectDto.setTrackJson(convertJsonAdd(root,userPrincipal));

        Long userId = userPrincipal.getId();
        String projectCode = projectDto.getProjectName();
        List<Project> projects = projectMapper.findProjectsByUserId(userId);
        List<String> projectNames = projects.stream()
                .map(Project::getProjectName) // 使用方法引用提取 projectCode
                .collect(Collectors.toList()); // 收集结果为 List
        if (projectNames.contains(projectDto.getProjectName())){
            logger.error("提交项目失败，, 用户ID: {}, 作品名称: {}", userPrincipal.getId(), projectDto.getProjectName(),"作品不能重复参赛");
            return Result.error(ResultCode.PROJECT_PROJECT_ALREADY_EXISTS);
        }

        Long projectId =  0L;
        try {
            Result<Long> projectPo  = createProject(projectDto,userPrincipal.getId());
            // 更新项目状态
            projectId = projectPo.getData();
            int result = projectMapper.updateStatus(projectId, ProjectStatus.SUBMITTED.getCode());
//            // 验证项目是否存在
            Project project = projectMapper.selectById(projectId);

            // 更新项目状态
            if (result > 0) {
                // 更新提交时间
                project.setSubmitTime(LocalDateTime.now());
                project.setUpdatedTime(LocalDateTime.now());
                projectMapper.updateById(project);

                logger.info("提交项目成功，项目ID: {}, 用户ID: {}", projectId, userPrincipal.getId());
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("提交项目失败，项目ID: {}, 用户ID: {}",projectId, userPrincipal.getId(), e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String convertJsonAdd(ObjectNode root,UserPrincipal userPrincipal){
        root.put("userId", userPrincipal.getId());
        root.put("username", userPrincipal.getUsername());
        root.put("email", userPrincipal.getEmail());
        return JsonUtils.toJson(root);
    }

    @Override
    @Transactional
    public Result<Void> withdrawProject(Long projectId, Long userId) {
        try {
            // 验证项目是否存在
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return Result.error(ResultCode.PROJECT_NOT_FOUND);
            }

            // 验证是否是项目所有者
            if (!project.getUserId().equals(userId)) {
                return Result.error(ResultCode.PROJECT_NOT_OWNER);
            }

            // 验证项目状态（只有已提交状态可以撤回）
            if (!ProjectStatus.SUBMITTED.getCode().equals(project.getStatus())) {
                return Result.error(ResultCode.PROJECT_STATUS_ERROR.getCode(), "只有已提交状态的项目可以撤回");
            }

            // 更新项目状态为草稿
            int result = projectMapper.updateStatus(projectId, ProjectStatus.DRAFT.getCode());
            if (result > 0) {
                // 清空提交时间
                project.setSubmitTime(null);
                project.setUpdatedTime(LocalDateTime.now());
                projectMapper.updateById(project);
                
                logger.info("撤回项目成功，项目ID: {}, 用户ID: {}", projectId, userId);
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("撤回项目失败，项目ID: {}, 用户ID: {}", projectId, userId, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public Result<Void> reviewProject(Long projectId, Integer status, String reviewComment) {
        try {
            // 验证项目是否存在
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return Result.error(ResultCode.PROJECT_NOT_FOUND);
            }

            // 验证状态有效性
            if (!isValidReviewStatus(status)) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "审核状态无效");
            }

            // 更新审核信息
            int result = projectMapper.updateReviewInfo(projectId, status, reviewComment);
            if (result > 0) {
                logger.info("审核项目成功，项目ID: {}, 状态: {}", projectId, status);
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("审核项目失败，项目ID: {}, 状态: {}", projectId, status, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<List<ProjectVo>> getProjectRankings(Long trackId, Integer limit) {
        try {
            List<ProjectVo> rankings = projectMapper.getProjectRankings(trackId, limit);
            return Result.success(rankings);
        } catch (Exception e) {
            logger.error("获取项目排名失败，赛道ID: {}, 限制: {}", trackId, limit, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public long countProjects() {
        try {
            return projectMapper.countProjects();
        } catch (Exception e) {
            logger.error("统计项目数量失败", e);
            return 0;
        }
    }

    @Override
    public long countSubmittedProjects() {
        try {
            return projectMapper.countSubmittedProjects();
        } catch (Exception e) {
            logger.error("统计已提交项目数量失败", e);
            return 0;
        }
    }

    @Override
    public long countTodayNewProjects() {
        try {
            return projectMapper.countTodayNewProjects();
        } catch (Exception e) {
            logger.error("统计今日新增项目数量失败", e);
            return 0;
        }
    }

    @Override
    public List<Map<String, Object>> countProjectsByTrack() {
        try {
            return projectMapper.countProjectsByTrack();
        } catch (Exception e) {
            logger.error("按赛道统计项目数量失败", e);
            return java.util.Collections.emptyList();
        }
    }
    /**
     * 按赛道统计项目数量
     */
    @Override
    public List<Map<String, Object>> getProjectsCountByTrack(UserPrincipal userPrincipal) {
        /* 1. 查出当前用户的赛道ID（查不到就是null） */
        Long trackId = userTracksMapper.selectTrackIdByUser(userPrincipal.getId());

        /* 2. 构造查询参数（复用已有的UserEditVo，只放赛道条件） */
        UserEditVo query = new UserEditVo();
        query.setTrackId(trackId);   // 为空时 SQL 里会去掉该条件
        /* 3. 调原有统计方法 */
        return projectMapper.selectProjectsCountByTrack(query);
//        return projectMapper.selectProjectsCountByTrack(userEditVo);
    }

    @Override
    public List<Map<String, Object>> countProjectsByStatus() {
        try {
            return projectMapper.countProjectsByStatus();
        } catch (Exception e) {
            logger.error("按状态统计项目数量失败", e);
            return java.util.Collections.emptyList();
        }
    }

    /**
     * 生成项目编号
     */
    private String generateProjectCode() {
        String prefix = "BTB";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String suffix = StringUtil.generateRandomString(4);
        return prefix + timestamp + suffix;
    }

    /**
     * 判断项目状态是否可编辑
     */
    private boolean isEditable(Integer status) {
        return ProjectStatus.DRAFT.getCode().equals(status);
    }

    /**
     * 验证项目信息是否完整
     */
    private boolean isProjectComplete(Project project) {
        return StringUtils.hasText(project.getProjectName()) &&
               StringUtils.hasText(project.getDescription()) &&
               project.getTrackId() != null &&
               StringUtils.hasText(project.getTeamInfo());
    }

    /**
     * 验证审核状态是否有效
     */
    private boolean isValidReviewStatus(Integer status) {
        return ProjectStatus.FIRST_REVIEW.getCode().equals(status) ||
               ProjectStatus.FIRST_PASS.getCode().equals(status) ||
               ProjectStatus.FIRST_REJECT.getCode().equals(status) ||
               ProjectStatus.SECOND_REVIEW.getCode().equals(status) ||
               ProjectStatus.SECOND_PASS.getCode().equals(status) ||
               ProjectStatus.SECOND_REJECT.getCode().equals(status) ||
               ProjectStatus.FINAL_REVIEW.getCode().equals(status) ||
               ProjectStatus.AWARDED.getCode().equals(status) ||
               ProjectStatus.ELIMINATED.getCode().equals(status);
    }

    /**
     * 转换为VO对象
     */
    private ProjectVo convertToVo(Project project) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project, projectVo);
        
        // 设置状态描述
        ProjectStatus status = ProjectStatus.getByCode(project.getStatus());
        if (status != null) {
            projectVo.setStatusName(status.getDescription());
        }
        // 将 String 类型的 trackJson 转换为 JsonNode 类型
        try {
            JsonNode trackJsonNode = objectMapper.readTree(project.getTrackJson());
            projectVo.setTrackJson(trackJsonNode);
        } catch (Exception e) {
            // 处理 JSON 转换失败的情况
            projectVo.setTrackJson(null);
        }
        // 设置可编辑性
        projectVo.setEditable(isEditable(project.getStatus()));
//        projectVo.setSubmitAable(ProjectStatus.DRAFT.getCode().equals(project.getStatus()));
        
        return projectVo;
    }
}
