package com.csmaxwell.sps.base.mapper;

import com.csmaxwell.sps.base.domain.SpsRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsRoleMapper extends BaseMapper<SpsRole> {

    List<SpsRole> getRoleList(@Param("userId") Long userId);
}
