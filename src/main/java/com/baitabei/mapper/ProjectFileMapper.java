package com.baitabei.mapper;

import com.baitabei.dto.ProjectDto;
import com.baitabei.entity.Project;
import com.baitabei.entity.ProjectFile;
import com.baitabei.vo.ProjectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目Mapper接口
 * 
 * @author MiniMax Agent
 */
@Mapper
public interface ProjectFileMapper extends BaseMapper<Project> {

    /**
     * 根据项目 ID 查询项目文件
     */
    @Select("SELECT * FROM project_files WHERE project_id = #{projectId} AND deleted = 0")
    List<ProjectFile> findByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据文件 ID 查询项目文件
     */
    @Select("SELECT * FROM project_files WHERE id = #{fileId} AND deleted = 0")
    ProjectFile findById(@Param("fileId") Long fileId);

    /**
     * 根据文件 URL 删除项目文件
     */
    int deleteByFileUrl(@Param("fileUrl") String fileUrl);
}