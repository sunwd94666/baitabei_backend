package com.baitabei.controller;

import com.baitabei.vo.UserAddVo;
import com.baitabei.vo.UserEditVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baitabei.common.result.Result;
import com.baitabei.dto.UserPageDto;
import com.baitabei.entity.User;
import com.baitabei.security.UserPrincipal;
import com.baitabei.service.UserService;
import com.baitabei.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 * 
 * @author MiniMax Agent
 */
@Tag(name = "用户管理", description = "用户管理")
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/profile")
    public Result<UserVo> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getUserById(userPrincipal.getId());
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/update")
    public Result<Void> updateProfile(@Valid @RequestBody UserEditVo user) {
        return userService.updateUser(user);
    }

    @PostMapping("/add")
    @Operation(summary = "新增用户", description = "用户名+角色ID+赛道ID，密码默认 BaitabeiAdmin")
    public Result<Long> add(@Valid @RequestBody UserAddVo vo) {
        return userService.addUser(vo);
    }

    @Operation(summary = "根据ID获取用户信息")
    @GetMapping("/{userId}")
    public Result<UserVo> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "根据用户名获取用户信息")
    @GetMapping("/username/{username}")
    public Result<UserVo> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
//    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<UserVo>> getUserPage(@Valid UserPageDto userPageDto) {
        return userService.getUserPage(userPageDto);
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/{userId}/status")
//    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateUserStatus(@PathVariable Long userId, 
                                        @RequestParam Integer status) {
        return userService.updateUserStatus(userId, status);
    }

    @Operation(summary = "批量更新用户状态")
    @PutMapping("/batch-status")
//    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchUpdateUserStatus(@RequestBody List<Long> userIds, 
                                            @RequestParam Integer status) {
        return userService.batchUpdateUserStatus(userIds, status);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping("/delete/batch")
//    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchDeleteUsers(@RequestBody List<Long> userIds) {
        return userService.batchDeleteUsers(userIds);
    }

    @Operation(summary = "检查用户名是否存在")
    @GetMapping("/exists/username/{username}")
    public Result<Boolean> existsByUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }

    @Operation(summary = "检查邮箱是否存在")
    @GetMapping("/exists/email/{email}")
    public Result<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return Result.success(exists);
    }

    @Operation(summary = "检查手机号是否存在")
    @GetMapping("/exists/phone/{phone}")
    public Result<Boolean> existsByPhone(@PathVariable String phone) {
        boolean exists = userService.existsByPhone(phone);
        return Result.success(exists);
    }

    @Operation(summary = "统计用户数量")
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> countUsers() {
        long count = userService.countUsers();
        return Result.success(count);
    }

    @Operation(summary = "统计激活用户数量")
    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> countActiveUsers() {
        long count = userService.countActiveUsers();
        return Result.success(count);
    }

    @Operation(summary = "统计今日新增用户数量")
    @GetMapping("/count/today-new")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> countTodayNewUsers() {
        long count = userService.countTodayNewUsers();
        return Result.success(count);
    }

    @Operation(summary = "统计在线用户数量")
    @GetMapping("/count/online")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> countOnlineUsers() {
        long count = userService.countOnlineUsers();
        return Result.success(count);
    }

    @Operation(summary = "获取用户角色")
    @GetMapping("/roles")
    public Result<List<String>> getUserRoles(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<String> roles = userService.getUserRoles(userPrincipal.getId());
        return Result.success(roles);
    }

    @Operation(summary = "获取用户权限")
    @GetMapping("/permissions")
    public Result<List<String>> getUserPermissions(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<String> permissions = userService.getUserPermissions(userPrincipal.getId());
        return Result.success(permissions);
    }
}
