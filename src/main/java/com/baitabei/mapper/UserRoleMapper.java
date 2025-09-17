package com.baitabei.mapper;

import com.baitabei.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Insert({
            "<script>",
            "INSERT INTO user_roles(user_id, role_id, created_time) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.userId}, #{item.roleId}, NOW())",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param("list") List<UserRole> list);
    /* 下面两个方法已在 Service 里用 Wrapper 实现，可不再写 XML */
}