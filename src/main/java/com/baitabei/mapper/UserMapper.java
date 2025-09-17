package com.baitabei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baitabei.dto.UserPageDto;
import com.baitabei.entity.User;
import com.baitabei.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 * 
 * @author MiniMax Agent
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 按角色统计用户数
     * @param roleId 为null时统计全部角色
     * @param roleCode 为null时统计全部角色
     * @return List<Map<roleId,roleCode,userCount>>
     */
    List<Map<String,Object>> countUsersByRole(@Param("roleId") Long roleId,
                                              @Param("roleCode") String roleCode);    /**
     * 根据用户名查询用户
     */
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     */
    User findByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     */
    User findByPhone(@Param("phone") String phone);

    /**
     * 根据用户名或邮箱查询用户
     */
    User findByUsernameOrEmail(@Param("identifier") String identifier);

    /**
     * 分页查询用户列表
     */
    IPage<UserVo> selectUserPage(Page<UserVo> page, @Param("dto") UserPageDto dto);

    /**
     * 根据角色查询用户列表
     */
    List<User> findUsersByRole(@Param("roleCode") String roleCode);

    /**
     * 查询用户角色列表
     */
    List<String> findUserRoles(@Param("userId") Long userId);

    /**
     * 查询用户权限列表
     */
    List<String> findUserPermissions(@Param("userId") Long userId);

    /**
     * 更新用户最后登录信息
     */
    int updateLastLogin(@Param("userId") Long userId, @Param("ip") String ip);

    /**
     * 更新用户状态
     */
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 批量更新用户状态
     */
    int batchUpdateStatus(@Param("userIds") List<Long> userIds, @Param("status") Integer status);

    /**
     * 统计用户数量
     */
    long countUsers();

    /**
     * 统计激活用户数量
     */
    long countActiveUsers();

    /**
     * 统计今日新增用户数量
     */
    long countTodayNewUsers();

    /**
     * 统计在线用户数量
     */
    long countOnlineUsers();
}
