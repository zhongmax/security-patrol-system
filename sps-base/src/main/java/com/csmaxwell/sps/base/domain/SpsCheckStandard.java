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
 * 巡查标准表
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sps_check_standard")
@ApiModel(value="SpsCheckStandard对象", description="巡查标准表")
public class SpsCheckStandard implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "巡查标准名称")
    private String name;

    @ApiModelProperty(value = "标准分数")
    private Integer score;

    @ApiModelProperty(value = "标准描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
