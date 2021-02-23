package com.csmaxwell.sps.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csmaxwell.sps.base.domain.SpsMenu;
import com.csmaxwell.sps.base.dto.MenuNode;
import com.csmaxwell.sps.base.mapper.SpsMenuMapper;
import com.csmaxwell.sps.base.service.SpsMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Service
public class SpsMenuServiceImpl extends ServiceImpl<SpsMenuMapper, SpsMenu> implements SpsMenuService {
    @Override
    public boolean create(SpsMenu spsMenu) {
        spsMenu.setCreateTime(new Date());
        updateLevel(spsMenu);
        return save(spsMenu);
    }

    /**
     * 需改菜单层级
     *
     * @param spsMenu
     */
    private void updateLevel(SpsMenu spsMenu) {
        if (spsMenu.getPid() == 0) {
            // 没有父菜单为一级菜单
            spsMenu.setLevel(0);
        } else {
            // 有父菜单时，根据父菜单level设置
            SpsMenu parentMenu = getById(spsMenu.getPid());
            if (parentMenu != null) {
                spsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                spsMenu.setLevel(0);
            }
        }
    }

    @Override
    public boolean update(Long id, SpsMenu spsMenu) {
        spsMenu.setId(id);
        updateLevel(spsMenu);
        return updateById(spsMenu);
    }

    @Override
    public Page<SpsMenu> list(Long pId, Integer pageSize, Integer pageNum) {
        Page<SpsMenu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SpsMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpsMenu::getPid, pId).orderByDesc(SpsMenu::getSort);
        return page(page, wrapper);
    }

    @Override
    public List<MenuNode> treeList() {
        List<SpsMenu> menuList = list();
        List<MenuNode> result = menuList.stream()
                .filter(menu -> menu.getPid().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public boolean updateHidden(Long id, Integer hidden) {
        SpsMenu spsMenu = new SpsMenu();
        spsMenu.setId(id);
        spsMenu.setHidden(hidden);
        return updateById(spsMenu);
    }

    /**
     * 将SpsMenu转化为SpsMenuNode并设置children属性
     */
    private MenuNode covertMenuNode(SpsMenu menu, List<SpsMenu> menuList) {
        MenuNode node = new MenuNode();
        BeanUtils.copyProperties(menu, node);
        List<MenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getPid().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
