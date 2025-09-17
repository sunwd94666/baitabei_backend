package com.baitabei.security;

import com.baitabei.constant.AppConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT令牌提供者
 *
 * @author MiniMax Agent
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret:baitabei2025SecretKeyForJWTTokenGeneration}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationTime;

    @Value("${app.jwt.refresh-expiration:604800000}")
    private long jwtRefreshExpirationTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // 使用HMAC-SHA算法生成安全的密钥
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 生成访问令牌
     * @param userId 用户ID
     * @return 访问令牌
     */
    public String generateAccessToken(Long userId) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationTime);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 生成刷新令牌
     * @param userId 用户ID
     * @return 刷新令牌
     */
    public String generateRefreshToken(Long userId) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtRefreshExpirationTime);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("type", "refresh")
                .signWith(secretKey)
                .compact();
    }

    /**
     * 从令牌中获取用户ID
     * @param token 令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            logger.error("从令牌获取用户ID失败", e);
            return null;
        }
    }

    /**
     * 验证令牌是否有效
     * @param token 令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("无效的JWT令牌: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT令牌已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的JWT令牌: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT令牌参数为空: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("验证JWT令牌失败: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取令牌过期时间
     * @param token 令牌
     * @return 过期时间
     */
    public Date getExpirationFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration();
        } catch (Exception e) {
            logger.error("获取令牌过期时间失败", e);
            return null;
        }
    }

    /**
     * 检查令牌是否即将过期
     * @param token 令牌
     * @param threshold 阈值（毫秒）
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token, long threshold) {
        try {
            Date expiration = getExpirationFromToken(token);
            if (expiration == null) {
                return true;
            }
            long timeUntilExpiration = expiration.getTime() - System.currentTimeMillis();
            return timeUntilExpiration <= threshold;
        } catch (Exception e) {
            logger.error("检查令牌是否即将过期失败", e);
            return true;
        }
    }

    /**
     * 获取令牌类型
     * @param token 令牌
     * @return 令牌类型
     */
    public String getTokenType(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return (String) claims.get("type");
        } catch (Exception e) {
            logger.error("获取令牌类型失败", e);
            return null;
        }
    }
}
