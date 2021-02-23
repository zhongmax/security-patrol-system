package com.csmaxwell.sps.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csmaxwell.sps.base.common.api.CommonPage;
import com.csmaxwell.sps.base.common.api.CommonResult;
import com.csmaxwell.sps.base.domain.SpsMenu;
import com.csmaxwell.sps.base.dto.MenuNode;
import com.csmaxwell.sps.base.service.SpsMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web端菜单Controller
 * Created by maxwell on 2021/2/23.
 */
@Controller
@Api(tags = "SpsMenuController", description = "菜单管理")
@RequestMapping("/web/menu")
public class SpsMenuController {

    @Autowired
    private SpsMenuService menuService;

    @ApiOperation("添加菜单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SpsMenu spsMenu) {
        boolean success = menuService.create(spsMenu);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改后台菜单")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody SpsMenu spsMenu) {
        boolean success = menuService.update(id, spsMenu);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据ID获取菜单详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SpsMenu> getItem(@PathVariable Long id) {
        SpsMenu spsMenu = menuService.getById(id);
        return CommonResult.success(spsMenu);
    }

    @ApiOperation("根据ID删除菜单")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = menuService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("分页查询菜单")
    @RequestMapping(value = "/list/{pId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<SpsMenu>> list(@PathVariable Long pId,
                                                  @RequestParam(value = "pageSize", defaultValue
                                                          = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue =
                                                          "1") Integer pageNum) {
        Page<SpsMenu> menuList = menuService.list(pId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(menuList));
    }

    @ApiOperation("树形结构返回所有菜单列表")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<MenuNode>> treeList() {
        List<MenuNode> list = menuService.treeList();
        return CommonResult.success(list);
    }

    @ApiOperation("修改菜单显示状态")
    @RequestMapping(value = "/updateHidden/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateHidden(@PathVariable Long id,
                                     @RequestParam("hidden") Integer hidden) {
        boolean success = menuService.updateHidden(id, hidden);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
