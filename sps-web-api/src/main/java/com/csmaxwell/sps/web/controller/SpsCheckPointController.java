package com.csmaxwell.sps.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csmaxwell.sps.base.common.api.CommonPage;
import com.csmaxwell.sps.base.common.api.CommonResult;
import com.csmaxwell.sps.base.domain.SpsCheckPoint;
import com.csmaxwell.sps.base.service.SpsCheckPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web端巡查点管理Controller
 * Created by maxwell on 2021/2/23.
 */
@Controller
@Api(tags = "SpsCheckPointController", description = "巡查点管理")
@RequestMapping("/web/point")
public class SpsCheckPointController {

    @Autowired
    private SpsCheckPointService checkPointService;

    @ApiOperation("巡查点添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SpsCheckPoint spsCheckPoint) {
        boolean success = checkPointService.create(spsCheckPoint);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("巡查点修改")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody SpsCheckPoint checkPoint) {
        checkPoint.setId(id);
        boolean success = checkPointService.updateById(checkPoint);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除巡查点")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = checkPointService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取所有巡查点")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SpsCheckPoint>> listAll() {
        List<SpsCheckPoint> checkPointList = checkPointService.list();
        return CommonResult.success(checkPointList);
    }

    @ApiOperation("巡查点分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<SpsCheckPoint>> list(@RequestParam(value = "keyword",
            required = false) String keyword,
                                                        @RequestParam(value = "pageSize",
                                                                defaultValue = "10") Integer pageSize,
                                                        @RequestParam(value = "pageNum",
                                                                defaultValue = "1") Integer pageNum) {
        Page<SpsCheckPoint> checkPointList = checkPointService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(checkPointList));
    }

}
