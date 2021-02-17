package com.csmaxwell.sps.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.domain.SpsRole;
import com.csmaxwell.sps.base.domain.SpsUser;
import com.csmaxwell.sps.base.dto.UserParam;
import com.csmaxwell.sps.base.dto.UpdateUserPasswordParam;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-01
 */
public interface SpsUserService extends IService<SpsUser> {

    /**
     * 根据用户名获取用户信息
     */
    SpsUser getUserByUsername(String username);

    /**
     * 获取指定用户的可访问权限
     */
    List<SpsPermission> getPermissionList(Long userId);

    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 注册
     */
    SpsUser register(UserParam userParam);

    /**
     * 登录（返回token）
     */
    String login(String username, String password);

    /**
     * 刷新token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户名或电话分页查询用户
     */
    Page<SpsUser> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改用户信息
     */
    boolean update(Long id, SpsUser user);

    /**
     * 删除指定用户
     */
    boolean delete(Long id);

    /**
     * 修改用户角色关系
     */
    @Transactional
    int updateRole(Long userId, List<Long> roleIds);

    /**
     * 获取用户角色列表
     */
    List<SpsRole> getRoleList(Long userId);

    /**
     * 修改密码
     */
    int updatePassword(UpdateUserPasswordParam userPasswordParam);
}
