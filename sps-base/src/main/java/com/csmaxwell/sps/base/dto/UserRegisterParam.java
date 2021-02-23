package com.csmaxwell.sps.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 用户登录参数
 * Created by maxwell on 2021/2/1.
 */
@Getter
@Setter
public class UserRegisterParam {

    @NotEmpty
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotEmpty
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotEmpty
    @ApiModelProperty(value = "电话", required = true)
    private String phone;

    @ApiModelProperty(value = "性别")
    private int gender;

    @ApiModelProperty(value = "年龄")
    private int age;
}
