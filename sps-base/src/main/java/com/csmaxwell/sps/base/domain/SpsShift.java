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
 * 排班表
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sps_shift")
@ApiModel(value="SpsShift对象", description="排班表")
public class SpsShift implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "排班日期")
    private Date date;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "巡查标准ID")
    private Long standardId;

    @ApiModelProperty(value = "开始时间")
    private Date start;

    @ApiModelProperty(value = "结束时间")
    private Date end;


}
