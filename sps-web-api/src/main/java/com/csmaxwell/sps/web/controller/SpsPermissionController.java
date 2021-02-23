package com.csmaxwell.sps.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csmaxwell.sps.base.common.api.CommonPage;
import com.csmaxwell.sps.base.common.api.CommonResult;
import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.security.component.DynamicSecurityMetadataSource;
import com.csmaxwell.sps.base.service.SpsPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web端权限管理Controller
 * Created by maxwell on 2021/2/21.
 */
@Controller
@Api(tags = "SpsPermissionController", description = "后台权限管理")
@RequestMapping("/web/perm")
public class SpsPermissionController {

    @Autowired
    private SpsPermissionService permissionService;
    @Autowired
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @ApiOperation("添加后台权限")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SpsPermission spsPermission) {
        boolean success = permissionService.create(spsPermission);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改权限")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody SpsPermission spsPermission) {
        boolean success = permissionService.update(id, spsPermission);
        dynamicSecurityMetadataSource.clearDataSource();
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据ID获取权限详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SpsPermission> getItem(@PathVariable Long id) {
        SpsPermission spsPermission = permissionService.getById(id);
        return CommonResult.success(spsPermission);
    }

    @ApiOperation("根据ID删除权限")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = permissionService.delete(id);
        dynamicSecurityMetadataSource.clearDataSource();
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<SpsPermission>> list(@RequestParam(required = false) Long categoryId,
                                                        @RequestParam(required = false) String nameKeyword,
                                                        @RequestParam(required = false) String urlKeyword,
                                                        @RequestParam(value = "pageSize",
                                                                defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum",
                                                                defaultValue = "1") Integer pageNum) {
        Page<SpsPermission> permissionList = permissionService.list(categoryId, nameKeyword, urlKeyword,
                pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(permissionList));
    }

    @ApiOperation("查询所有权限")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SpsPermission>> listAll() {
        List<SpsPermission> permissionList = permissionService.list();
        return CommonResult.success(permissionList);
    }
}
