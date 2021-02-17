package com.csmaxwell.sps.base.mapper;

import com.csmaxwell.sps.base.domain.SpsMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsMenuMapper extends BaseMapper<SpsMenu> {

    List<SpsMenu> getMenuList(@Param("userId") Long userId);

    List<SpsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
}
