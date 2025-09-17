package com.baitabei.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baitabei.common.result.Result;
import com.baitabei.common.result.ResultCode;
import com.baitabei.constant.AppConstants;
import com.baitabei.dto.UserPageDto;
import com.baitabei.entity.User;
import com.baitabei.entity.UserRole;
import com.baitabei.mapper.UserMapper;
import com.baitabei.mapper.UserRoleMapper;
import com.baitabei.service.UserRoleService;
import com.baitabei.service.UserService;
import com.baitabei.vo.RoleVo;
import com.baitabei.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 
 * @author MiniMax Agent
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {


    @Override
    public boolean save(UserRole entity) {
        return UserRoleService.super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<UserRole> entityList) {
        return UserRoleService.super.saveBatch(entityList);
    }

    @Override
    public boolean saveBatch(Collection<UserRole> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<UserRole> entityList) {
        return UserRoleService.super.saveOrUpdateBatch(entityList);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<UserRole> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean removeById(Serializable id) {
        return UserRoleService.super.removeById(id);
    }

    @Override
    public boolean removeById(Serializable id, boolean useFill) {
        return UserRoleService.super.removeById(id, useFill);
    }

    @Override
    public boolean removeById(UserRole entity) {
        return UserRoleService.super.removeById(entity);
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        return UserRoleService.super.removeByMap(columnMap);
    }

    @Override
    public boolean remove(Wrapper<UserRole> queryWrapper) {
        return UserRoleService.super.remove(queryWrapper);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        return UserRoleService.super.removeByIds(list);
    }

    @Override
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        return UserRoleService.super.removeByIds(list, useFill);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return UserRoleService.super.removeBatchByIds(list);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return UserRoleService.super.removeBatchByIds(list, useFill);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, int batchSize) {
        return UserRoleService.super.removeBatchByIds(list, batchSize);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        return UserRoleService.super.removeBatchByIds(list, batchSize, useFill);
    }

    @Override
    public boolean updateById(UserRole entity) {
        return UserRoleService.super.updateById(entity);
    }

    @Override
    public boolean update(Wrapper<UserRole> updateWrapper) {
        return UserRoleService.super.update(updateWrapper);
    }

    @Override
    public boolean update(UserRole entity, Wrapper<UserRole> updateWrapper) {
        return UserRoleService.super.update(entity, updateWrapper);
    }

    @Override
    public boolean updateBatchById(Collection<UserRole> entityList) {
        return UserRoleService.super.updateBatchById(entityList);
    }

    @Override
    public boolean updateBatchById(Collection<UserRole> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(UserRole entity) {
        return false;
    }

    @Override
    public UserRole getById(Serializable id) {
        return UserRoleService.super.getById(id);
    }

    @Override
    public List<UserRole> listByIds(Collection<? extends Serializable> idList) {
        return UserRoleService.super.listByIds(idList);
    }

    @Override
    public List<UserRole> listByMap(Map<String, Object> columnMap) {
        return UserRoleService.super.listByMap(columnMap);
    }

    @Override
    public UserRole getOne(Wrapper<UserRole> queryWrapper) {
        return UserRoleService.super.getOne(queryWrapper);
    }

    @Override
    public UserRole getOne(Wrapper<UserRole> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<UserRole> queryWrapper) {
        return Collections.emptyMap();
    }

    @Override
    public <V> V getObj(Wrapper<UserRole> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public long count() {
        return UserRoleService.super.count();
    }

    @Override
    public long count(Wrapper<UserRole> queryWrapper) {
        return UserRoleService.super.count(queryWrapper);
    }

    @Override
    public List<UserRole> list(Wrapper<UserRole> queryWrapper) {
        return UserRoleService.super.list(queryWrapper);
    }

    @Override
    public List<UserRole> list() {
        return UserRoleService.super.list();
    }

    @Override
    public <E extends IPage<UserRole>> E page(E page, Wrapper<UserRole> queryWrapper) {
        return UserRoleService.super.page(page, queryWrapper);
    }

    @Override
    public <E extends IPage<UserRole>> E page(E page) {
        return UserRoleService.super.page(page);
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<UserRole> queryWrapper) {
        return UserRoleService.super.listMaps(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps() {
        return UserRoleService.super.listMaps();
    }

    @Override
    public List<Object> listObjs() {
        return UserRoleService.super.listObjs();
    }

    @Override
    public <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return UserRoleService.super.listObjs(mapper);
    }

    @Override
    public List<Object> listObjs(Wrapper<UserRole> queryWrapper) {
        return UserRoleService.super.listObjs(queryWrapper);
    }

    @Override
    public <V> List<V> listObjs(Wrapper<UserRole> queryWrapper, Function<? super Object, V> mapper) {
        return UserRoleService.super.listObjs(queryWrapper, mapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<UserRole> queryWrapper) {
        return UserRoleService.super.pageMaps(page, queryWrapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page) {
        return UserRoleService.super.pageMaps(page);
    }

    @Override
    public BaseMapper<UserRole> getBaseMapper() {
        return null;
    }

    @Override
    public Class<UserRole> getEntityClass() {
        return null;
    }

    @Override
    public QueryChainWrapper<UserRole> query() {
        return UserRoleService.super.query();
    }

    @Override
    public LambdaQueryChainWrapper<UserRole> lambdaQuery() {
        return UserRoleService.super.lambdaQuery();
    }

    @Override
    public LambdaQueryChainWrapper<UserRole> lambdaQuery(UserRole entity) {
        return UserRoleService.super.lambdaQuery(entity);
    }

    @Override
    public KtQueryChainWrapper<UserRole> ktQuery() {
        return UserRoleService.super.ktQuery();
    }

    @Override
    public KtUpdateChainWrapper<UserRole> ktUpdate() {
        return UserRoleService.super.ktUpdate();
    }

    @Override
    public UpdateChainWrapper<UserRole> update() {
        return UserRoleService.super.update();
    }

    @Override
    public LambdaUpdateChainWrapper<UserRole> lambdaUpdate() {
        return UserRoleService.super.lambdaUpdate();
    }

    @Override
    public boolean saveOrUpdate(UserRole entity, Wrapper<UserRole> updateWrapper) {
        return UserRoleService.super.saveOrUpdate(entity, updateWrapper);
    }
}
