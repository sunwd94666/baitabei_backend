package com.baitabei.mapper;

import com.baitabei.vo.UserEditVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baitabei.dto.ProjectDto;
import com.baitabei.entity.Project;
import com.baitabei.vo.ProjectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目Mapper接口
 * 
 * @author MiniMax Agent
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 根据项目编号查询项目
     */
    Project findByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 分页查询项目列表
     */
    IPage<ProjectVo> selectProjectPage(Page<ProjectVo> page, @Param("dto") ProjectDto dto);

    /**
     * 根据用户ID查询项目列表
     */
    List<Project> findProjectsByUserId(@Param("userId") Long userId);

    /**
     * 根据赛道ID查询项目列表
     */
    List<Project> findProjectsByTrackId(@Param("trackId") Long trackId);

    /**
     * 根据状态查询项目列表
     */
    List<Project> findProjectsByStatus(@Param("status") Integer status);

    /**
     * 更新项目状态
     */
    int updateStatus(@Param("projectId") Long projectId, @Param("status") Integer status);

    /**
     * 更新项目审核信息
     */
    int updateReviewInfo(@Param("projectId") Long projectId, 
                        @Param("status") Integer status,
                        @Param("reviewComment") String reviewComment);

    /**
     * 更新项目得分
     */
    int updateScore(@Param("projectId") Long projectId, 
                   @Param("finalScore") java.math.BigDecimal finalScore,
                   @Param("ranking") Integer ranking);

    /**
     * 统计项目数量
     */
    long countProjects();

    /**
     * 统计已提交项目数量
     */
    long countSubmittedProjects();

    /**
     * 统计今日新增项目数量
     */
    long countTodayNewProjects();

    /**
     * 按赛道统计项目数量
     */
    List<java.util.Map<String, Object>> countProjectsByTrack();

    /**
     * 按赛道统计项目数量
     */
    List<java.util.Map<String, Object>> selectProjectsCountByTrack(UserEditVo userEditVo);

    /**
     * 按状态统计项目数量
     */
    List<java.util.Map<String, Object>> countProjectsByStatus();

    /**
     * 获取项目排名列表
     */
    List<ProjectVo> getProjectRankings(@Param("trackId") Long trackId, 
                                      @Param("limit") Integer limit);

    /**
     * 检查项目编号是否存在
     */
    boolean existsByProjectCode(@Param("projectCode") String projectCode);
}
