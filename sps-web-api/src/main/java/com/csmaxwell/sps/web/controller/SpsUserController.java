package com.csmaxwell.sps.web.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csmaxwell.sps.base.common.api.CommonPage;
import com.csmaxwell.sps.base.common.api.CommonResult;
import com.csmaxwell.sps.base.domain.SpsRole;
import com.csmaxwell.sps.base.domain.SpsUser;
import com.csmaxwell.sps.base.dto.UpdateUserPasswordParam;
import com.csmaxwell.sps.base.dto.UserLoginParam;
import com.csmaxwell.sps.base.dto.UserRegisterParam;
import com.csmaxwell.sps.base.service.SpsRoleService;
import com.csmaxwell.sps.base.service.SpsUserService;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Web端用户Controller
 * Created by maxwell on 2021/2/16.
 */
@Controller
@Api(tags = "SpsUserController", description = "用户管理")
@RequestMapping("/web/user")
public class SpsUserController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private SpsUserService userService;
    @Autowired
    private SpsRoleService roleService;

    @Autowired
    private Producer kaptchaProducer;

    @ApiOperation(value = "注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SpsUser> register(@Validated @RequestBody UserRegisterParam userRegisterParam) {
        SpsUser spsUser = userService.register(userRegisterParam);
        if (spsUser == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(spsUser);
    }

    @ApiOperation(value = "登录后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@Validated @RequestBody UserLoginParam userLoginParam) {
        String token = userService.login(userLoginParam.getUsername(),
                userLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult kaptcha(HttpServletRequest request) {
        String kaptcha = doKaptcha(request);
        System.out.println("获取验证码: " + kaptcha);
        if (kaptcha != null) {
            System.out.println("进来");
            return CommonResult.success(kaptcha);
        }
        return CommonResult.failed();
    }

    private String doKaptcha(HttpServletRequest request) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        HttpSession session = request.getSession();
        session.setAttribute("kaptcha", text);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encode(outputStream.toByteArray());
            String captchaBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
            return captchaBase64;
        } catch (IOException e) {
            return null;
        }
    }

    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = userService.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed("token已经过期！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getUserInfo(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        SpsUser spsUser = userService.getUserByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", spsUser.getUsername());
        data.put("menus", roleService.getMenuList(spsUser.getId()));
        List<SpsRole> roleList = userService.getRoleList(spsUser.getId());
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> roles =
                    roleList.stream().map(SpsRole::getName).collect(Collectors.toList());
            data.put("roles", roles);
        }
        return CommonResult.success(data);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<SpsUser>> list(@RequestParam(value = "keyword", required =
            false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue
                                                          = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue =
                                                          "1") Integer pageNum) {
        Page<SpsUser> userList = userService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(userList));
    }

    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SpsUser> getItem(@PathVariable Long id) {
        SpsUser user = userService.getById(id);
        return CommonResult.success(user);
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody SpsUser user) {
        boolean success = userService.update(id, user);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改指定用户密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@Validated @RequestBody UpdateUserPasswordParam updatePasswordParam) {
        int status = userService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("找不到该用户");
        } else if (status == -3) {
            return CommonResult.failed("旧密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = userService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改帐号状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id,
                                     @RequestParam(value = "status") Integer status) {
        SpsUser spsUser = new SpsUser();
        spsUser.setStatus(status);
        boolean success = userService.update(id, spsUser);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRole(@RequestParam("userId") Long userId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = userService.updateRole(userId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SpsRole>> getRoleList(@PathVariable Long userId) {
        List<SpsRole> roleList = userService.getRoleList(userId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("用户名是否占用")
    @RequestMapping(value = "/existUsername/{username}")
    @ResponseBody
    public CommonResult existUsername(@PathVariable String username) {
        SpsUser spsUser = userService.getUserByUsername(username);
        return CommonResult.success(spsUser);
    }

    @ApiOperation("手机号是否占用")
    @RequestMapping(value = "/existPhone/{phone}")
    @ResponseBody
    public CommonResult existPhone(@PathVariable String phone) {
        SpsUser spsUser = userService.getUserByPhone(phone);
        return CommonResult.success(spsUser);
    }

}
