package com.csmaxwell.sps.base.dto;

import com.csmaxwell.sps.base.domain.SpsMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 菜单节点封装
 * Created by maxwell on 2021/2/10.
 */
@Getter
@Setter
public class MenuNode extends SpsMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<MenuNode> children;
}
