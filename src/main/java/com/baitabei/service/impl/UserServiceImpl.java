package com.baitabei.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baitabei.entity.UserRole;
import com.baitabei.entity.UserTrack;
import com.baitabei.mapper.UserRoleMapper;
import com.baitabei.mapper.UserTracksMapper;
import com.baitabei.service.UserRoleService;
import com.baitabei.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baitabei.common.result.Result;
import com.baitabei.common.result.ResultCode;
import com.baitabei.constant.AppConstants;
import com.baitabei.dto.UserPageDto;
import com.baitabei.entity.User;
import com.baitabei.exception.BusinessException;
import com.baitabei.mapper.UserMapper;
import com.baitabei.service.UserService;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 
 * @author MiniMax Agent
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String DEFAULT_PASSWORD = "BaitabeiAdmin";

    @Autowired
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;   // 直接删/插

    @Resource
    private UserRoleService userRoleService; // 批量保存
    @Autowired
    private UserTracksMapper userTracksMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Map<String,Object>> countUsersByRole(Long roleId, String roleCode) {
        return userMapper.countUsersByRole(roleId, roleCode);
    }

    @Override
    public Result<UserVo> getUserById(Long userId) {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);

            return Result.success(userVo);
        } catch (Exception e) {
            logger.error("获取用户信息失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<UserVo> getUserByUsername(String username) {
        try {
            User user = userMapper.findByUsername(username);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);

            return Result.success(userVo);
        } catch (Exception e) {
            logger.error("根据用户名获取用户信息失败，用户名: {}", username, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<IPage<UserVo>> getUserPage(UserPageDto userPageDto) {
        try {
            // 设置分页参数
            if (userPageDto.getPageNum() == null || userPageDto.getPageNum() < 1) {
                userPageDto.setPageNum(AppConstants.DEFAULT_PAGE_NUM);
            }
            if (userPageDto.getPageSize() == null || userPageDto.getPageSize() < 1) {
                userPageDto.setPageSize(AppConstants.DEFAULT_PAGE_SIZE);
            }
            if (userPageDto.getPageSize() > AppConstants.MAX_PAGE_SIZE) {
                userPageDto.setPageSize(AppConstants.MAX_PAGE_SIZE);
            }

            Page<UserVo> page = new Page<>(userPageDto.getPageNum(), userPageDto.getPageSize());
            IPage<UserVo> userPage = userMapper.selectUserPage(page, userPageDto);

            return Result.success(userPage);
        } catch (Exception e) {
            logger.error("分页查询用户列表失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> addUser(UserAddVo vo) {
        // 1. 重复用户名校验
        if (existsByUsername(vo.getUsername())) {
            return Result.error(ResultCode.USERNAME_ALREADY_EXISTS);
        }

        // 2. 构造 User
        User user = new User();
        user.setUsername(vo.getUsername());
        user.setEmail("");
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setStatus(1);          // 启用
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(user.getCreatedTime());

        // 3. 插入主表
        userMapper.insert(user);
        Long userId = user.getId();

        // 4. 写角色关联
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(vo.getRoleId());
        userRole.setCreatedTime(LocalDateTime.now());
        userRoleMapper.insert(userRole);

        // 5. 写赛道关联
        UserTrack userTrack = new UserTrack();
        userTrack.setUserId(userId);
        userTrack.setTrackId(vo.getTrackId());
        userTrack.setCreatedTime(LocalDateTime.now());
        userTracksMapper.insert(userTrack);

        logger.info("新增用户成功，userId={}", userId);
        return Result.success(userId);
    }

    @Override
    @Transactional
    public Result<Void> updateUser(UserEditVo userDto) {
        Long userId = Long.valueOf(userDto.getId());
        if (userId == null) {
            return Result.error(ResultCode.BAD_REQUEST);
        }

        // 1. 数据库原记录
        User dbUser = userMapper.selectById(userId);
        if (dbUser == null || dbUser.getDeleted() == 1) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 2. 唯一性校验（只在校验字段非空且发生变化时执行）
        // 2.1 用户名
        if (StringUtils.isNotBlank(userDto.getUsername())
                && !userDto.getUsername().equals(dbUser.getUsername())) {
            if (userMapper.findByUsername(userDto.getUsername()) != null) {
                return Result.error(ResultCode.USERNAME_ALREADY_EXISTS);
            }
        }
        // 2.2 邮箱
        if (StringUtils.isNotBlank(userDto.getEmail())
                && !userDto.getEmail().equals(dbUser.getEmail())) {
            if (userMapper.findByEmail(userDto.getEmail()) != null) {
                return Result.error(ResultCode.EMAIL_ALREADY_EXISTS);
            }
        }
        // 2.3 手机号
        if (StringUtils.isNotBlank(userDto.getPhone())
                && !userDto.getPhone().equals(dbUser.getPhone())) {
            if (userMapper.findByPhone(userDto.getPhone()) != null) {
                return Result.error(ResultCode.PHONE_ALREADY_EXISTS);
            }
        }

        // 3. 只更新非空字段（跳过 id、roles、系统字段）
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.eq("id", userId);
        ReflectionUtils.doWithFields(UserEditVo.class, f -> {
            f.setAccessible(true);
            try {
                Object val = f.get(userDto);
                String field = f.getName();
                if (val != null && !"id".equals(field) && !"roleId".equals(field) && !"trackId".equals(field)
                        && !"createdTime".equals(field) && !"updatedTime".equals(field)
                        && !"deleted".equals(field) && !"password".equals(field)) {
                    uw.set(StrUtil.toUnderlineCase(field), val);
                }
            } catch (IllegalAccessException ignored) {
            }
        });

        if (StringUtils.isNotBlank(uw.getSqlSet())) {
            userMapper.update(null, uw);
        }
        Long roleId = userDto.getRoleId();
        /// 4. 仅更新 user_roles 关联表（增量）
        if (roleId != null) {
            // ① 删光
            userRoleMapper.delete(
                    new QueryWrapper<UserRole>().eq("user_id", userId));

            // ② 批量插入
            // ② 批量插入（一条 SQL）
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreatedTime(LocalDateTime.now());
            userRoleMapper.insert(userRole);
        }
        Long trackId = userDto.getTrackId();
        /// 5. 仅更新 user_tracks 关联表（增量）
        if (roleId != null) {
            // ① 删光
            userTracksMapper.delete(
                    new QueryWrapper<UserTrack>().eq("user_id", userId));
            // ② 批量插入
            // ② 批量插入（一条 SQL）
            UserTrack userTrack = new UserTrack();
            userTrack.setUserId(userId);
            userTrack.setTrackId(trackId);
            userTrack.setCreatedTime(LocalDateTime.now());
            userTracksMapper.insert(userTrack);
        }

        return Result.success();
    }

    /**
     * 只更新 UserVo 中 非 null 字段 + 角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserSelective(UserVo vo) {
        Long userId = vo.getId();
        Assert.notNull(userId, "用户ID不能为空");

        // 1. 基本字段：只更非空
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.eq("id", userId);
        // 利用 MyBatis-Plus 3.5+ 提供的 lambda 反射填充
        ReflectionUtils.doWithFields(UserVo.class, f -> {
            f.setAccessible(true);
            try {
                Object val = f.get(vo);
                if (val != null && !"id".equals(f.getName()) && !"roles".equals(f.getName())) {
                    // 把 UserVo 字段值拷贝到 UpdateWrapper set
                    uw.set(StrUtil.toUnderlineCase(f.getName()), val);
                }
            } catch (IllegalAccessException ignore) { }
        });

        if (uw.getSqlSet() != null) {
            userMapper.update(null, uw);
        }

        // 2. 角色：先删后插
        if (vo.getRoles() != null) {
            List<Long> roleIds = vo.getRoles()
                    .stream()
                    .map(RoleVo::getId)
                    .collect(Collectors.toList());
            userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", userId));
            if (CollUtil.isNotEmpty(roleIds)) {
                List<UserRole> list = roleIds.stream()
                        .map(rid -> new UserRole(userId, rid))
                        .collect(Collectors.toList());
                userRoleService.saveBatch(list);
            }
        }
    }


    @Override
    @Transactional
    public Result<Void> updateUserStatus(Long userId, Integer status) {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }

            int result = userMapper.updateStatus(userId, status);
            if (result > 0) {
                logger.info("更新用户状态成功，用户ID: {}, 状态: {}", userId, status);
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("更新用户状态失败，用户ID: {}, 状态: {}", userId, status, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public Result<Void> batchUpdateUserStatus(List<Long> userIds, Integer status) {
        try {
            if (userIds == null || userIds.isEmpty()) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "用户ID列表不能为空");
            }

            int result = userMapper.batchUpdateStatus(userIds, status);
            if (result > 0) {
                logger.info("批量更新用户状态成功，影响行数: {}, 状态: {}", result, status);
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("批量更新用户状态失败，用户ID列表: {}, 状态: {}", userIds, status, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public Result<Void> deleteUser(Long userId) {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }

            int result = userMapper.deleteById(userId);
            if (result > 0) {
                logger.info("删除用户成功，用户ID: {}", userId);
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("删除用户失败，用户ID: {}", userId, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public Result<Void> batchDeleteUsers(List<Long> userIds) {
        try {
            if (userIds == null || userIds.isEmpty()) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "用户ID列表不能为空");
            }

            int result = userMapper.deleteBatchIds(userIds);
            if (result > 0) {
                logger.info("批量删除用户成功，影响行数: {}", result);
                return Result.success();
            } else {
                return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("批量删除用户失败，用户ID列表: {}", userIds, e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            User user = userMapper.findByUsername(username);
            return user != null;
        } catch (Exception e) {
            logger.error("检查用户名是否存在失败，用户名: {}", username, e);
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            User user = userMapper.findByEmail(email);
            return user != null;
        } catch (Exception e) {
            logger.error("检查邮箱是否存在失败，邮箱: {}", email, e);
            return false;
        }
    }

    @Override
    public boolean existsByPhone(String phone) {
        try {
            User user = userMapper.findByPhone(phone);
            return user != null;
        } catch (Exception e) {
            logger.error("检查手机号是否存在失败，手机号: {}", phone, e);
            return false;
        }
    }

    @Override
    public long countUsers() {
        try {
            return userMapper.countUsers();
        } catch (Exception e) {
            logger.error("统计用户数量失败", e);
            return 0;
        }
    }

    @Override
    public long countActiveUsers() {
        try {
            return userMapper.countActiveUsers();
        } catch (Exception e) {
            logger.error("统计激活用户数量失败", e);
            return 0;
        }
    }

    @Override
    public long countTodayNewUsers() {
        try {
            return userMapper.countTodayNewUsers();
        } catch (Exception e) {
            logger.error("统计今日新增用户数量失败", e);
            return 0;
        }
    }

    @Override
    public long countOnlineUsers() {
        try {
            return userMapper.countOnlineUsers();
        } catch (Exception e) {
            logger.error("统计在线用户数量失败", e);
            return 0;
        }
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        try {
            return userMapper.findUserRoles(userId);
        } catch (Exception e) {
            logger.error("获取用户角色列表失败，用户ID: {}", userId, e);
//            return List.of();
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        try {
            return userMapper.findUserPermissions(userId);
        } catch (Exception e) {
            logger.error("获取用户权限列表失败，用户ID: {}", userId, e);
//            return List.of();
            return Collections.emptyList();
        }
    }
}
