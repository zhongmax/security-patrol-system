package com.csmaxwell.sps.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户信息没有密码字段
 * Created by maxwell on 2021/2/21.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserVo对象", description="UserVo")
public class UserVo {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "用户状态")
    private Integer status;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
