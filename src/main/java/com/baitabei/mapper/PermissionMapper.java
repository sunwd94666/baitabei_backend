package com.baitabei.mapper;

import com.baitabei.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限数据访问层
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    
}
