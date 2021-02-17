package com.csmaxwell.sps.base.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sps_permission")
@ApiModel(value="SpsPermission对象", description="权限表")
public class SpsPermission implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限")
    private String permission;

    @ApiModelProperty(value = "权限描述")
    private String description;

    @ApiModelProperty(value = "权限分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
