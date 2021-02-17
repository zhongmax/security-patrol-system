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
 * 巡查点签到表
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sps_sign")
@ApiModel(value="SpsSign对象", description="巡查点签到表")
public class SpsSign implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "签到点ID")
    private Long pointId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "签到日期")
    private Date date;

    @ApiModelProperty(value = "签到状态")
    private Integer status;


}
