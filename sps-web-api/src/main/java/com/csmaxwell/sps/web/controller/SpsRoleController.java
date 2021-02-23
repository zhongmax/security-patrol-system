package com.csmaxwell.sps.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csmaxwell.sps.base.common.api.CommonPage;
import com.csmaxwell.sps.base.common.api.CommonResult;
import com.csmaxwell.sps.base.domain.SpsMenu;
import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.domain.SpsRole;
import com.csmaxwell.sps.base.service.SpsPermissionService;
import com.csmaxwell.sps.base.service.SpsRoleMenuService;
import com.csmaxwell.sps.base.service.SpsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web端角色Controller
 * Created by maxwell on 2021/2/21.
 */
@Controller
@Api(tags = "SpsRoleController", description = "角色管理")
@RequestMapping("/web/role")
public class SpsRoleController {

    @Autowired
    private SpsRoleService roleService;
    @Autowired
    private SpsRoleMenuService menuService;
    @Autowired
    private SpsPermissionService permissionService;

    @ApiOperation("角色添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SpsRole role) {
        boolean success = roleService.create(role);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改角色")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody SpsRole role) {
        role.setId(id);
        boolean success = roleService.updateById(role);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        boolean success = roleService.delete(ids);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取所有角色")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SpsRole>> listAll() {
        List<SpsRole> roleList = roleService.list();
        return CommonResult.success(roleList);
    }

    @ApiOperation("根据角色名分页获取角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<SpsRole>> list(@RequestParam(value = "keyword", required =
            false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue
                                                          = "10") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue =
                                                          "1") Integer pageNum) {
        Page<SpsRole> roleList = roleService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(roleList));
    }

    @ApiOperation("修改角色状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id,
                                     @RequestParam(value = "status") Integer status) {
        SpsRole spsRole = new SpsRole();
        spsRole.setId(id);
        spsRole.setStatus(status);
        boolean success = roleService.updateById(spsRole);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "获取角色相关菜单")
    @RequestMapping(value = "/listMenu/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SpsMenu>> listMenu(@PathVariable Long roleId) {
        List<SpsMenu> roleList = roleService.listMenu(roleId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("获取角色相关权限")
    @RequestMapping(value = "/listPermission/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SpsPermission>> listPermission(@PathVariable Long roleId) {
        List<SpsPermission> roleList = roleService.listPermission(roleId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("给角色分配菜单")
    @RequestMapping(value = "/allocMenu", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult allocMenu(@RequestParam(value = "roleId") Long roleId,
                                  @RequestParam(value = "menuIds") List<Long> menuIds) {
        System.out.println("菜单ID为" + menuIds.toString());
        int count = roleService.allocMenu(roleId, menuIds);
        return CommonResult.success(count);
    }

    @ApiOperation("给角色分配资源")
    @RequestMapping(value = "/allocPermission", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult allocResource(@RequestParam(value = "roleId") Long roleId,
                                      @RequestParam(value = "permissionIds") List<Long> permissionIds) {
        int count = roleService.allocPermission(roleId, permissionIds);
        return CommonResult.success(count);
    }
}
