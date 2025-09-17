package com.baitabei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baitabei.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * 
 * @author MiniMax Agent
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色编码查询角色
     */
    Role findByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询启用的角色列表
     */
    List<Role> findEnabledRoles();

    /**
     * 根据用户ID查询角色列表
     */
    List<Role> findRolesByUserId(@Param("userId") Long userId);

    /**
     * 检查角色编码是否存在
     */
    boolean existsByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 更新角色状态
     */
    int updateStatus(@Param("roleId") Long roleId, @Param("status") Integer status);

    /**
     * 删除用户角色关联
     */
    int deleteUserRolesByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除角色权限关联
     */
    int deleteRolePermissionsByRoleId(@Param("roleId") Long roleId);
}
