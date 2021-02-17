package com.csmaxwell.sps.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csmaxwell.sps.base.domain.SpsPermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsPermissionService extends IService<SpsPermission> {
    /**
     * 添加资源
     */
    boolean create(SpsPermission spsPermission);

    /**
     * 修改资源
     */
    boolean update(Long id, SpsPermission spsPermission);

    /**
     * 删除资源
     */
    boolean delete(Long id);

    /**
     * 分页查询资源
     */
    Page<SpsPermission> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

}
