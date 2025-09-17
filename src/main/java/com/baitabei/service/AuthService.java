package com.baitabei.service;

import com.baitabei.common.result.Result;
import com.baitabei.dto.LoginDto;
import com.baitabei.dto.RegisterDto;
import com.baitabei.vo.LoginVo;

/**
 * 认证服务接口
 * 
 * @author MiniMax Agent
 */
public interface AuthService {

    /**
     * 用户登录
     * @param loginDto 登录信息
     * @return 登录结果
     */
    Result<LoginVo> login(LoginDto loginDto);

    /**
     * 用户注册
     * @param registerDto 注册信息
     * @return 注册结果
     */
    Result<Void> register(RegisterDto registerDto);

    /**
     * 用户退出登录
     * @param token 令牌
     * @return 退出结果
     */
    Result<Void> logout(String token);

    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @return 新令牌
     */
    Result<LoginVo> refreshToken(String refreshToken);

    /**
     * 验证令牌
     * @param token 令牌
     * @return 是否有效
     */
    boolean validateToken(String token);

    /**
     * 从令牌中获取用户ID
     * @param token 令牌
     * @return 用户ID
     */
    Long getUserIdFromToken(String token);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return 发送结果
     */
    Result<Void> sendEmailVerificationCode(String email);

    /**
     * 验证邮箱验证码
     * @param email 邮箱
     * @param code 验证码
     * @return 验证结果
     */
    Result<Void> verifyEmailCode(String email, String code);

    /**
     * 发送手机短信验证码
     * @param phone 手机号
     * @return 发送结果
     */
    Result<Void> sendSmsVerificationCode(String phone);

    /**
     * 验证手机短信验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 验证结果
     */
    Result<Void> verifySmsCode(String phone, String code);

    /**
     * 重置密码
     * @param email 邮箱
     * @param newPassword 新密码
     * @param verificationCode 验证码
     * @return 重置结果
     */
    Result<Void> resetPassword(String email, String newPassword, String verificationCode);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    Result<Void> changePassword(Long userId, String oldPassword, String newPassword);
}
