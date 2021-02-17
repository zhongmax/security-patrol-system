package com.csmaxwell.sps.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csmaxwell.sps.base.domain.*;
import com.csmaxwell.sps.base.dto.UserParam;
import com.csmaxwell.sps.base.dto.UpdateUserPasswordParam;
import com.csmaxwell.sps.base.mapper.SpsPermissionMapper;
import com.csmaxwell.sps.base.mapper.SpsRoleMapper;
import com.csmaxwell.sps.base.mapper.SpsUserMapper;
import com.csmaxwell.sps.base.security.domain.SecurityUserDetails;
import com.csmaxwell.sps.base.service.SpsUserRoleService;
import com.csmaxwell.sps.base.service.SpsUserService;
import com.csmaxwell.sps.base.service.UserCacheService;
import com.csmaxwell.sps.base.common.exception.Asserts;
import com.csmaxwell.sps.base.common.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-01
 */
@Service
public class SpsUserServiceImpl extends ServiceImpl<SpsUserMapper, SpsUser> implements SpsUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpsUserServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private SpsUserRoleService userRoleService;
    @Autowired
    private SpsRoleMapper roleMapper;
    @Autowired
    private SpsPermissionMapper permissionMapper;

    @Override
    public SpsUser getUserByUsername(String username) {
        SpsUser user = userCacheService.getUser(username);
        if (user != null) {
            return user;
        }
        QueryWrapper<SpsUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpsUser::getUsername, username);
        List<SpsUser> adminList = list(wrapper);
        if (adminList != null && adminList.size() > 0) {
            user = adminList.get(0);
            userCacheService.setUser(user);
            return user;
        }
        return null;
    }

    @Override
    public List<SpsPermission> getPermissionList(Long userId) {
        List<SpsPermission> permissionList = userCacheService.getPermissionList(userId);
        if (CollUtil.isNotEmpty(permissionList)) {
            return permissionList;
        }
        permissionList = permissionMapper.getPermissionList(userId);
        if (CollUtil.isNotEmpty(permissionList)) {
            userCacheService.setPermissionList(userId, permissionList);
        }
        return permissionList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 获取用户信息
        SpsUser user = getUserByUsername(username);
        if (user != null) {
            List<SpsPermission> permissionList = getPermissionList(user.getId());
            return new SecurityUserDetails(user, permissionList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public SpsUser register(UserParam userParam) {
        SpsUser spsUser = new SpsUser();
        BeanUtils.copyProperties(userParam, spsUser);
        spsUser.setCreateTime(new Date());
        spsUser.setStatus(1);
        //查询是否有相同用户名的用户
        QueryWrapper<SpsUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpsUser::getUsername, spsUser.getUsername());
        List<SpsUser> SpsUserList = list(wrapper);
        if (SpsUserList.size() > 0) {
            return null;
        }
        // 将密码进行加密
        String encodePassword = passwordEncoder.encode(spsUser.getPassword());
        spsUser.setPassword(encodePassword);
        baseMapper.insert(spsUser);
        return spsUser;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        // 密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                Asserts.fail("密码不正确");
            }
            if (!userDetails.isEnabled()) {
                Asserts.fail("账号已停用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public Page<SpsUser> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<SpsUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SpsUser> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SpsUser> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(SpsUser::getUsername, keyword);
            lambda.or().like(SpsUser::getPhone, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public boolean update(Long id, SpsUser user) {
        user.setId(id);
        SpsUser rawUser = getById(id);
        if (rawUser.getPassword().equals(user.getPassword())) {
            // 与原加密密码相同不需要修改
            user.setPassword(null);
        } else {
            // 与原加密密码不同，需要加密修改
            if (StrUtil.isEmpty(user.getPassword())) {
                user.setPassword(null);
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        boolean success = updateById(user);
        userCacheService.delUser(id);
        return success;
    }

    @Override
    public boolean delete(Long id) {
        userCacheService.delUser(id);
        boolean success = removeById(id);
        userCacheService.delPermissionList(id);
        return success;
    }

    @Override
    public int updateRole(Long userId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        // 先删除原来的关系
        QueryWrapper<SpsUserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpsUserRole::getUserId, userId);
        userRoleService.remove(wrapper);
        // 建立新关系
        if (!CollectionUtil.isEmpty(roleIds)) {
            List<SpsUserRole> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                SpsUserRole userRole = new SpsUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                list.add(userRole);
            }
            userRoleService.saveBatch(list);
        }
        userCacheService.delPermissionList(userId);
        return count;
    }

    @Override
    public List<SpsRole> getRoleList(Long userId) {
        return roleMapper.getRoleList(userId);
    }

    @Override
    public int updatePassword(UpdateUserPasswordParam param) {
        if (StrUtil.isEmpty(param.getUsername())
                || StrUtil.isEmpty(param.getOldPassword())
                || StrUtil.isEmpty(param.getNewPassword())) {
            return -1;
        }
        QueryWrapper<SpsUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpsUser::getUsername, param.getUsername());
        List<SpsUser> userList = list(wrapper);
        if (CollUtil.isNotEmpty(userList)) {
            return -2;
        }
        SpsUser spsUser = userList.get(0);
        if (!passwordEncoder.matches(param.getOldPassword(), spsUser.getPassword())) {
            return -3;
        }
        spsUser.setPassword(passwordEncoder.encode(param.getNewPassword()));
        updateById(spsUser);
        userCacheService.delUser(spsUser.getId());
        return 1;
    }

}
