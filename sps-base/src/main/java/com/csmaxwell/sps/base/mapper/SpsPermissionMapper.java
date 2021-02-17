package com.csmaxwell.sps.base.mapper;

import com.csmaxwell.sps.base.domain.SpsPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsPermissionMapper extends BaseMapper<SpsPermission> {

    /**
     * 获取用户所有可访问权限
     */
    List<SpsPermission> getPermissionList(@Param("userId") Long userId);

    /**
     * 根据角色ID获取权限
     */
    List<SpsPermission> getPermissionListByRoleId(@Param("roleId") Long roleId);
}
