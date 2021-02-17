package com.csmaxwell.sps.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csmaxwell.sps.base.domain.SpsMenu;
import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.domain.SpsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsRoleService extends IService<SpsRole> {
    /**
     * 添加角色
     */
    boolean create(SpsRole role);

    /**
     * 批量删除角色
     */
    boolean delete(List<Long> ids);

    /**
     * 分页获取角色列表
     */
    Page<SpsRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据管理员ID获取对应菜单
     */
    List<SpsMenu> getMenuList(Long userId);

    /**
     * 获取角色相关菜单
     */
    List<SpsMenu> listMenu(Long roleId);

    /**
     * 获取角色相关权限
     */
    List<SpsPermission> listPermission(Long roleId);

    /**
     * 给角色分配菜单
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 给角色分配资源
     */
    @Transactional
    int allocPermission(Long roleId, List<Long> permissionIds);
}
