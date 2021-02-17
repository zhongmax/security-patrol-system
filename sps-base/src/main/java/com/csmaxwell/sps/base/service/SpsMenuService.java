package com.csmaxwell.sps.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csmaxwell.sps.base.domain.SpsMenu;
import com.csmaxwell.sps.base.dto.MenuNode;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsMenuService extends IService<SpsMenu> {
    /**
     * 创建菜单
     */
    boolean create(SpsMenu spsMenu);

    /**
     * 修改菜单
     */
    boolean update(Long id, SpsMenu spsMenu);

    /**
     * 分页查询菜单
     */
    Page<SpsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有列表
     */
    List<MenuNode> treeList();

    /**
     * 修改菜单显示状态
     */
    boolean updateHidden(Long id, Integer hidden);
}
