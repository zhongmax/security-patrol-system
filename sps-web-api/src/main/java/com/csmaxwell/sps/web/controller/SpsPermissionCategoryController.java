package com.csmaxwell.sps.web.controller;

import com.csmaxwell.sps.base.common.api.CommonResult;
import com.csmaxwell.sps.base.domain.SpsPermissionCategory;
import com.csmaxwell.sps.base.service.SpsPermissionCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web端权限分类管理Controller
 * Created by maxwell on 2021/2/23.
 */
@Controller
@Api(tags = "SpsPermissionCategoryController", description = "权限分类管理")
@RequestMapping("/web/permCategory")
public class SpsPermissionCategoryController {

    @Autowired
    private SpsPermissionCategoryService permissionCategoryService;

    @ApiOperation("查询所有权限分类")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SpsPermissionCategory>> listAll() {
        List<SpsPermissionCategory> permissionCategoryList = permissionCategoryService.listAll();
        return CommonResult.success(permissionCategoryList);
    }

    @ApiOperation("添加权限分类")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SpsPermissionCategory spsPermissionCategory) {
        boolean success = permissionCategoryService.create(spsPermissionCategory);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改后台资源分类")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id,
                               @RequestBody SpsPermissionCategory spsPermissionCategory) {
        spsPermissionCategory.setId(id);
        boolean success = permissionCategoryService.updateById(spsPermissionCategory);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = permissionCategoryService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
