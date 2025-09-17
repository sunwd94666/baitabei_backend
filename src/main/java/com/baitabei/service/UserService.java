package com.baitabei.service;

import com.baitabei.vo.UserAddVo;
import com.baitabei.vo.UserEditVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baitabei.common.result.Result;
import com.baitabei.dto.UserPageDto;
import com.baitabei.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 * 
 * @author MiniMax Agent
 */
public interface UserService {
    List<Map<String,Object>> countUsersByRole(Long roleId, String roleCode);
    /**
     * 根据ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    Result<UserVo> getUserById(Long userId);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    Result<UserVo> getUserByUsername(String username);

    /**
     * 分页查询用户列表
     * @param userPageDto 查询条件
     * @return 用户列表
     */
    Result<IPage<UserVo>> getUserPage(UserPageDto userPageDto);

    /**
     * 新增用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    Result<Long> addUser(UserAddVo user);
    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    Result<Void> updateUser(UserEditVo user);

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 状态
     * @return 更新结果
     */
    Result<Void> updateUserStatus(Long userId, Integer status);

    /**
     * 批量更新用户状态
     * @param userIds 用户ID列表
     * @param status 状态
     * @return 更新结果
     */
    Result<Void> batchUpdateUserStatus(List<Long> userIds, Integer status);

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除结果
     */
    Result<Void> deleteUser(Long userId);

    /**
     * 批量删除用户
     * @param userIds 用户ID列表
     * @return 删除结果
     */
    Result<Void> batchDeleteUsers(List<Long> userIds);

    /**
     * 验证用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 验证邮箱是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 验证手机号是否存在
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 统计用户数量
     * @return 用户数量
     */
    long countUsers();

    /**
     * 统计激活用户数量
     * @return 激活用户数量
     */
    long countActiveUsers();

    /**
     * 统计今日新增用户数量
     * @return 今日新增用户数量
     */
    long countTodayNewUsers();

    /**
     * 统计在线用户数量
     * @return 在线用户数量
     */
    long countOnlineUsers();

    /**
     * 获取用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<String> getUserRoles(Long userId);

    /**
     * 获取用户权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(Long userId);
}
