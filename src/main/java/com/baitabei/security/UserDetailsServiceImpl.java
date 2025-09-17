package com.baitabei.security;

import com.baitabei.entity.User;
import com.baitabei.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security用户详情服务实现
 * 
 * @author MiniMax Agent
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsernameOrEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return createUserPrincipal(user);
    }

    /**
     * 根据用户ID加载用户信息
     */
    public UserDetails loadUserByUserId(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        return createUserPrincipal(user);
    }

    /**
     * 创建用户主体对象
     */
    private UserPrincipal createUserPrincipal(User user) {
        // 获取用户角色列表
        List<String> roles = userMapper.findUserRoles(user.getId());
        
        // 获取用户权限列表
        List<String> permissions = userMapper.findUserPermissions(user.getId());
        
        // 转换为GrantedAuthority集合
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        
        // 添加权限
        authorities.addAll(permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .enabled(user.getStatus() == 1)
                .build();
    }
}
