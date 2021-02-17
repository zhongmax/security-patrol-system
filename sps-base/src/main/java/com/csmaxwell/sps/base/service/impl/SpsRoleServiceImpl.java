package com.csmaxwell.sps.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csmaxwell.sps.base.domain.*;
import com.csmaxwell.sps.base.mapper.SpsMenuMapper;
import com.csmaxwell.sps.base.mapper.SpsPermissionMapper;
import com.csmaxwell.sps.base.mapper.SpsRoleMapper;
import com.csmaxwell.sps.base.service.SpsRoleMenuService;
import com.csmaxwell.sps.base.service.SpsRolePermissionService;
import com.csmaxwell.sps.base.service.SpsRoleService;
import com.csmaxwell.sps.base.service.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Service
public class SpsRoleServiceImpl extends ServiceImpl<SpsRoleMapper, SpsRole> implements SpsRoleService {
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private SpsRoleMenuService roleMenuService;
    @Autowired
    private SpsRolePermissionService rolePermissionService;
    @Autowired
    private SpsMenuMapper menuMapper;
    @Autowired
    private SpsPermissionMapper permissionMapper;

    @Override
    public boolean create(SpsRole role) {
        role.setCreateTime(new Date());
        return save(role);
    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean success = removeByIds(ids);
        userCacheService.delPermissionListByRoleIds(ids);
        return false;
    }

    @Override
    public Page<SpsRole> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<SpsRole> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SpsRole> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SpsRole> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(SpsRole::getName, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public List<SpsMenu> getMenuList(Long userId) {
        return menuMapper.getMenuList(userId);
    }

    @Override
    public List<SpsMenu> listMenu(Long roleId) {
        return menuMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<SpsPermission> listPermission(Long roleId) {
        return permissionMapper.getPermissionListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        // 删除原有关系
        QueryWrapper<SpsRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpsRoleMenu::getRoleId, roleId);
        roleMenuService.remove(wrapper);
        // 批量插入新关系
        List<SpsRoleMenu> roleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            SpsRoleMenu roleMenu = new SpsRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        roleMenuService.saveBatch(roleMenuList);
        return menuIds.size();
    }

    @Override
    public int allocPermission(Long roleId, List<Long> permissionIds) {
        // 删除原有关系
        QueryWrapper<SpsRolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpsRolePermission::getRoleId, roleId);
        rolePermissionService.remove(wrapper);
        // 批量插入新关系
        List<SpsRolePermission> rolePermissionList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            SpsRolePermission rolePermission = new SpsRolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionList);
        userCacheService.delPermissionListByRole(roleId);
        return permissionIds.size();
    }
}
