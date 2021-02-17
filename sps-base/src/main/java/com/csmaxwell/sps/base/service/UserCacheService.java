package com.csmaxwell.sps.base.service;

import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.domain.SpsUser;

import java.util.List;

/**
 * 用户缓存管理Service
 * Created by maxwell on 2021/2/16.
 */
public interface UserCacheService {
    /**
     * 获取缓存用户信息
     */
    SpsUser getUser(String username);

    /**
     * 设置缓存后台用户信息
     */
    void setUser(SpsUser user);

    /**
     * 获取缓存用户资源列表
     */
    List<SpsPermission> getPermissionList(Long userId);

    /**
     * 设置用户资源列表
     */
    void setPermissionList(Long userId, List<SpsPermission> permissionList);

    /**
     * 删除用户缓存
     */
    void delUser(Long userId);

    /**
     * 删除用户相关权利列表缓存
     */
    void delPermissionList(Long userId);

    /**
     * 当角色相关信息权限改变时，删除相关用户缓存
     */
    void delPermissionListByRole(Long roleId);

    /**
     * 当角色相关资源信息改变时，删除相关用户缓存
     */
    void delPermissionListByRoleIds(List<Long> roleIds);

    /**
     * 当资源信息改变时，删除权限用户缓存
     */
    void delPermissionListByPermission(Long permissionId);
}
