package com.csmaxwell.sps.base.mapper;

import com.csmaxwell.sps.base.domain.SpsUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsUserMapper extends BaseMapper<SpsUser> {

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getUserIdList(@Param("permissionId") Long permissionId);
}
