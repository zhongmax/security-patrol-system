package com.csmaxwell.sps.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.common.service.RedisService;
import com.csmaxwell.sps.base.service.SpsUserRoleService;
import com.csmaxwell.sps.base.service.SpsUserService;
import com.csmaxwell.sps.base.service.UserCacheService;
import com.csmaxwell.sps.base.domain.SpsUser;
import com.csmaxwell.sps.base.domain.SpsUserRole;
import com.csmaxwell.sps.base.mapper.SpsUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户缓存管理Service实现类
 * Created by maxwell on 2021/2/16.
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {

    @Autowired
    private SpsUserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SpsUserMapper userMapper;
    @Autowired
    private SpsUserRoleService userRoleService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.user}")
    private String REDIS_KEY_USER;
    @Value("${redis.key.permissionList}")
    private String REDIS_KEY_PERMISSION_LIST;

    @Override
    public SpsUser getUser(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_USER + ":" + username;
        return (SpsUser) redisService.get(key);
    }

    @Override
    public void setUser(SpsUser user) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_USER + ":" + user.getUsername();
        redisService.set(key, user, REDIS_EXPIRE);
    }

    @Override
    public List<SpsPermission> getPermissionList(Long userId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSION_LIST + ":" + userId;
        return (List<SpsPermission>) redisService.get(key);
    }

    @Override
    public void setPermissionList(Long userId, List<SpsPermission> permissionList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSION_LIST + ":" + userId;
        redisService.set(key, permissionList, REDIS_EXPIRE);
    }

    @Override
    public void delUser(Long userId) {
        SpsUser user = userService.getById(userId);
        if (user != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_USER + ":" + user.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public void delPermissionList(Long userId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSION_LIST + ":" + userId;
        redisService.del(key);
    }

    @Override
    public void delPermissionListByRole(Long roleId) {
        QueryWrapper<SpsUserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpsUserRole::getRoleId,roleId);
        List<SpsUserRole> userRoleList = userRoleService.list(wrapper);
        if (CollUtil.isNotEmpty(userRoleList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSION_LIST + ":";
            List<String> keys = userRoleList.stream().map(relation -> keyPrefix + relation.getUserId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delPermissionListByRoleIds(List<Long> roleIds) {
        QueryWrapper<SpsUserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SpsUserRole::getRoleId,roleIds);
        List<SpsUserRole> userRoleList = userRoleService.list(wrapper);
        if (CollUtil.isNotEmpty(userRoleList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSION_LIST + ":";
            List<String> keys = userRoleList.stream().map(relation -> keyPrefix + relation.getUserId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delPermissionListByPermission(Long permissionId) {
        List<Long> userIdList = userMapper.getUserIdList(permissionId);
        if (CollUtil.isNotEmpty(userIdList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSION_LIST + ":";
            List<String> keys = userIdList.stream().map(userId -> keyPrefix + userId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }
}
