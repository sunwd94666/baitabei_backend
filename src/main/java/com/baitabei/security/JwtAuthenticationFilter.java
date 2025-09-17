package com.baitabei.security;

import com.baitabei.constant.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * JWT认证过滤器
 *
 * @author MiniMax Agent
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final List<String> WHITE_LIST =
           Arrays.asList("/api/auth/", "/api/public/", "/doc.html", "/webjars/", "/swagger-resources/", "/v2/api-docs");
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);
        String uri = request.getRequestURI();
        logger.info("JWT拦截路径：" + uri);
        logger.info("servletPath={}"+ request.getServletPath());
        // 白名单直接放行
//        if (WHITE_LIST.stream().anyMatch(uri::startsWith)) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        // 检查是否为Swagger路径

        // 检查是否为Swagger路径
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            // 检查token是否在Redis中存在（防止被黑名单）
            if (redisTemplate.hasKey(AppConstants.REDIS_TOKEN_PREFIX + jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

                if (userId != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUserId(userId);
                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中获取JWT令牌
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AppConstants.JWT_HEADER_NAME);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AppConstants.JWT_TOKEN_PREFIX)) {
            return bearerToken.substring(AppConstants.JWT_TOKEN_PREFIX.length());
        }
        return null;
    }

}
