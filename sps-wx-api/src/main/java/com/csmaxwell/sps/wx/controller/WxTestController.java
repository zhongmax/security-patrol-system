package com.csmaxwell.sps.wx.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * S
 * Created by maxwell on 2021/2/16.
 */
@Controller
@Api(tags = "WxTestController", description = "测试controller")
@RequestMapping("/test")
public class WxTestController {

    @GetMapping("/hello")
    @ResponseBody
    public String test() {
        return "Hello World";
    }
}
