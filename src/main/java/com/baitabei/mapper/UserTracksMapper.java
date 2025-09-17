package com.baitabei.mapper;

import com.baitabei.entity.UserRole;
import com.baitabei.entity.UserTrack;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserTracksMapper extends BaseMapper<UserTrack> {
    @Insert({
            "<script>",
            "INSERT INTO user_tracks(user_id, track_id, created_time) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.userId}, #{item.trackId}, NOW())",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param("list") List<UserRole> list);
    /* 下面两个方法已在 Service 里用 Wrapper 实现，可不再写 XML */
    /**
     * 根据用户ID查询赛道ID，最多一条
     */
    @Select("SELECT track_id FROM user_tracks WHERE user_id = #{userId} LIMIT 1")
    Long selectTrackIdByUser(@Param("userId") Long userId);
}