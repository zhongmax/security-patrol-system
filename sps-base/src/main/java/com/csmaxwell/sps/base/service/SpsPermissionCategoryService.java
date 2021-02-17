package com.csmaxwell.sps.base.service;

import com.csmaxwell.sps.base.domain.SpsPermissionCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限分类表 服务类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsPermissionCategoryService extends IService<SpsPermissionCategory> {
    /**
     * 创建资源分类
     * @param spsPermissionCategory
     * @return
     */
    boolean create(SpsPermissionCategory spsPermissionCategory);

    /**
     * 获取所有资源分类
     * @return
     */
    List<SpsPermissionCategory> listAll();
}
