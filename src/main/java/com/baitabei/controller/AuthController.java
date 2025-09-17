package com.baitabei.controller;

import com.baitabei.common.result.Result;
import com.baitabei.dto.LoginDto;
import com.baitabei.dto.RegisterDto;
import com.baitabei.service.AuthService;
import com.baitabei.vo.LoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 认证控制器
 * 
 * @author MiniMax Agent
 */
@Tag(name = "认证管理", description = "认证管理")
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVo> login(@Valid @RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @Operation(summary = "用户退出")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return authService.logout(token);
    }

    @Operation(summary = "刷新令牌")
    @PostMapping("/refresh-token")
    public Result<LoginVo> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/send-email-code")
    public Result<Void> sendEmailCode(@RequestParam("email") String email) {
        return authService.sendEmailVerificationCode(email);
    }

    @Operation(summary = "验证邮箱验证码")
    @PostMapping("/verify-email-code")
    public Result<Void> verifyEmailCode(@RequestParam("email") String email,
                                       @RequestParam("code") String code) {
        return authService.verifyEmailCode(email, code);
    }

    @Operation(summary = "发送手机短信验证码")
    @PostMapping("/send-sms-code")
    public Result<Void> sendSmsCode(@RequestParam("phone") String phone) {
        return authService.sendSmsVerificationCode(phone);
    }

    @Operation(summary = "验证手机短信验证码")
    @PostMapping("/verify-sms-code")
    public Result<Void> verifySmsCode(@RequestParam("phone") String phone,
                                     @RequestParam("code") String code) {
        return authService.verifySmsCode(phone, code);
    }

    @Operation(summary = "重置密码")
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestParam("email") String email,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestParam("verificationCode") String verificationCode) {
        return authService.resetPassword(email, newPassword, verificationCode);
    }

    @Operation(summary = "修改密码")
    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestParam("userId") Long userId,
                                      @RequestParam("oldPassword") String oldPassword,
                                      @RequestParam("newPassword") String newPassword) {
        return authService.changePassword(userId, oldPassword, newPassword);
    }

    @Operation(summary = "验证令牌")
    @GetMapping("/validate-token")
    public Result<Boolean> validateToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        boolean isValid = authService.validateToken(token);
        return Result.success(isValid);
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
