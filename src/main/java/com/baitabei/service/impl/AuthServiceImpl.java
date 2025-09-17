package com.baitabei.service.impl;

import com.baitabei.common.result.Result;
import com.baitabei.common.result.ResultCode;
import com.baitabei.constant.AppConstants;
import com.baitabei.dto.LoginDto;
import com.baitabei.dto.RegisterDto;
import com.baitabei.entity.User;
import com.baitabei.mapper.UserMapper;
import com.baitabei.security.JwtTokenProvider;
import com.baitabei.service.AuthService;
import com.baitabei.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
/**
 * 认证服务实现类
 * 
 * @author MiniMax Agent
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Resource   //
    private JavaMailSender mailSender;   //
    @Value("${spring.mail.username}")   // 对应 yml 里的 username
    private String sendFrom;
    /**
     * 明文 → 密文
     */
    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }
    @Override
    public Result<LoginVo> login(LoginDto loginDto) {
        try {
            // 查询用户
            User user = userMapper.findByUsernameOrEmail(loginDto.getUsername());
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }

            // 验证用户状态
            if (user.getStatus() == 0) {
                return Result.error(ResultCode.USER_DISABLED);
            }
            // 验证密码
            if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return Result.error(ResultCode.PASSWORD_ERROR);
            }

            // 生成token
            String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(AppConstants.JWT_EXPIRATION_TIME);

            // 保存token到Redis
            redisTemplate.opsForValue().set(
                AppConstants.REDIS_TOKEN_PREFIX + accessToken, 
                user.getId(), 
                AppConstants.JWT_EXPIRATION_TIME, 
                TimeUnit.MILLISECONDS
            );

            // 更新最后登录信息
            userMapper.updateLastLogin(user.getId(), getClientIp());

            // 构造返回对象
            LoginVo loginVo = new LoginVo(
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getEmail(),
                user.getAvatar(),
                accessToken,
                refreshToken,
                expiresAt
            );

            logger.info("用户登录成功，用户ID: {}, 用户名: {}", user.getId(), user.getUsername());
            return Result.success(loginVo);
        } catch (Exception e) {
            logger.error("用户登录失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public Result<Void> register(RegisterDto registerDto) {
        try {
            // 验证确认密码
            if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
                return Result.error(ResultCode.CONFIRM_PASSWORD_NOT_MATCH);
            }

            // 检查用户名是否存在
            if (userMapper.findByUsername(registerDto.getUsername()) != null) {
                return Result.error(ResultCode.USERNAME_ALREADY_EXISTS);
            }

            // 检查邮箱是否存在
            if (userMapper.findByEmail(registerDto.getEmail()) != null) {
                return Result.error(ResultCode.EMAIL_ALREADY_EXISTS);
            }

            // 检查手机号是否存在（如果提供了手机号）
            if (StringUtils.hasText(registerDto.getPhone()) && 
                userMapper.findByPhone(registerDto.getPhone()) != null) {
                return Result.error(ResultCode.PHONE_ALREADY_EXISTS);
            }

            // 创建用户
            User user = new User();
            BeanUtils.copyProperties(registerDto, user);
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setStatus(1); // 默认激活状态
            user.setEmailVerified(0);
            user.setPhoneVerified(0);

            // 设置 created_time 和 updated_time
            user.setCreatedTime(LocalDateTime.now());
            user.setUpdatedTime(LocalDateTime.now());
            userMapper.insert(user);

            logger.info("用户注册成功，用户名: {}, 邮箱: {}", registerDto.getUsername(), registerDto.getEmail());
            return Result.success();
        } catch (Exception e) {
            logger.error("用户注册失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<Void> logout(String token) {
        try {
            if (StringUtils.hasText(token)) {
                // 从Redis中删除token
                redisTemplate.delete(AppConstants.REDIS_TOKEN_PREFIX + token);
                logger.info("用户退出登录，token: {}", token);
            }
            return Result.success();
        } catch (Exception e) {
            logger.error("用户退出登录失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<LoginVo> refreshToken(String refreshToken) {
        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                return Result.error(ResultCode.TOKEN_INVALID);
            }

            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
            User user = userMapper.selectById(userId);
            if (user == null || user.getStatus() == 0) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }

            // 生成新的token
            String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(AppConstants.JWT_EXPIRATION_TIME / 1000);

            // 保存新token到Redis
            redisTemplate.opsForValue().set(
                AppConstants.REDIS_TOKEN_PREFIX + newAccessToken,
                userId,
                AppConstants.JWT_EXPIRATION_TIME,
                TimeUnit.MILLISECONDS
            );

            LoginVo loginVo = new LoginVo(
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getEmail(),
                user.getAvatar(),
                newAccessToken,
                newRefreshToken,
                expiresAt
            );

            return Result.success(loginVo);
        } catch (Exception e) {
            logger.error("刷新token失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return false;
            }
            return jwtTokenProvider.validateToken(token) && 
                   redisTemplate.hasKey(AppConstants.REDIS_TOKEN_PREFIX + token);
        } catch (Exception e) {
            logger.error("验证token失败", e);
            return false;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return null;
            }
            return jwtTokenProvider.getUserIdFromToken(token);
        } catch (Exception e) {
            logger.error("从token获取用户ID失败", e);
            return null;
        }
    }

    @Override
    public Result<Void> sendEmailVerificationCode(String email) {
        // 1. 生成 6 位随机验证码
        String code = generateVerificationCode();

        // 2. 先写 Redis，防止邮件发成功后 Redis 写失败
        String redisKey = AppConstants.REDIS_VERIFICATION_CODE_PREFIX + email;
        redisTemplate.opsForValue()
                .set(redisKey, code,
                        AppConstants.VERIFICATION_CODE_EXPIRE_TIME,
                        TimeUnit.SECONDS);

        try {
            // 3. 异步发送邮件（Spring 默认线程池）
            CompletableFuture.runAsync(() -> doSend(email, code));
            logger.info("发送邮箱验证码成功，邮箱: {}, 验证码: {}", email, code);
            return Result.success();
        } catch (Exception e) {
            // 4. 邮件发送失败，把 Redis 里的验证码删掉，防止脏数据
            redisTemplate.delete(redisKey);
            logger.error("发送邮箱验证码失败", e);
            return Result.error(ResultCode.SEND_EMAIL_ERROR);
        }
    }

    /**
     * 真正发邮件的方法，异常全部抛出去，让上层异步捕获
     */
    private void doSend(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sendFrom);          // 配置文件里读
            helper.setTo(email);
            helper.setSubject("您的验证码");

            // 简单文本
            helper.setText("验证码：" + code + "，5 分钟内有效，请勿泄露。");

            // 如果想用 HTML 模板：
            // Context ctx = new Context();
            // ctx.setVariable("code", code);
            // String html = templateEngine.process("verification-email", ctx);
            // helper.setText(html, true);

            mailSender.send(message);
        } catch (Exception e) {
            // 抛出去，让 CompletableFuture 统一处理
            throw new RuntimeException(e);
        }
    }

//    /**
//     * 6 位数字验证码
//     */
//    private String generateVerificationCode() {
//        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
//    }


    @Override
    public Result<Void> verifyEmailCode(String email, String code) {
        try {
            String savedCode = (String) redisTemplate.opsForValue().get(
                AppConstants.REDIS_VERIFICATION_CODE_PREFIX + email
            );
            
            if (savedCode == null) {
                return Result.error(ResultCode.VERIFICATION_CODE_EXPIRED);
            }
            
            if (!savedCode.equals(code)) {
                return Result.error(ResultCode.VERIFICATION_CODE_ERROR);
            }
            
            // 删除验证码
            redisTemplate.delete(AppConstants.REDIS_VERIFICATION_CODE_PREFIX + email);
            
            return Result.success();
        } catch (Exception e) {
            logger.error("验证邮箱验证码失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<Void> sendSmsVerificationCode(String phone) {
        try {
            // 生成6位随机验证码
            String code = generateVerificationCode();
            
            // 保存到Redis，有效期5分钟
            redisTemplate.opsForValue().set(
                AppConstants.REDIS_VERIFICATION_CODE_PREFIX + phone,
                code,
                AppConstants.VERIFICATION_CODE_EXPIRE_TIME,
                TimeUnit.SECONDS
            );

            // TODO: 发送短信
            logger.info("发送短信验证码成功，手机号: {}, 验证码: {}", phone, code);
            return Result.success();
        } catch (Exception e) {
            logger.error("发送短信验证码失败", e);
            return Result.error(ResultCode.SEND_SMS_ERROR);
        }
    }

    @Override
    public Result<Void> verifySmsCode(String phone, String code) {
        try {
            String savedCode = (String) redisTemplate.opsForValue().get(
                AppConstants.REDIS_VERIFICATION_CODE_PREFIX + phone
            );
            
            if (savedCode == null) {
                return Result.error(ResultCode.VERIFICATION_CODE_EXPIRED);
            }
            
            if (!savedCode.equals(code)) {
                return Result.error(ResultCode.VERIFICATION_CODE_ERROR);
            }
            
            // 删除验证码
            redisTemplate.delete(AppConstants.REDIS_VERIFICATION_CODE_PREFIX + phone);
            
            return Result.success();
        } catch (Exception e) {
            logger.error("验证短信验证码失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<Void> resetPassword(String email, String newPassword, String verificationCode) {
        try {
            // 验证邮箱验证码
            Result<Void> verifyResult = verifyEmailCode(email, verificationCode);
            if (!verifyResult.isSuccess()) {
                return verifyResult;
            }
            
            // 查找用户
            User user = userMapper.findByEmail(email);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }
            
            // 更新密码
            user.setPassword(passwordEncoder.encode(newPassword));
            userMapper.updateById(user);
            
            logger.info("重置密码成功，邮箱: {}", email);
            return Result.success();
        } catch (Exception e) {
            logger.error("重置密码失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<Void> changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST);
            }
            
            // 验证旧密码
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return Result.error(ResultCode.OLD_PASSWORD_ERROR);
            }
            
            // 更新密码
            user.setPassword(passwordEncoder.encode(newPassword));
            userMapper.updateById(user);
            
            logger.info("修改密码成功，用户ID: {}", userId);
            return Result.success();
        } catch (Exception e) {
            logger.error("修改密码失败", e);
            return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 生成6位随机验证码
     */
    private String generateVerificationCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp() {
        // TODO: 从HttpServletRequest获取真实IP
        return "127.0.0.1";
    }
}
