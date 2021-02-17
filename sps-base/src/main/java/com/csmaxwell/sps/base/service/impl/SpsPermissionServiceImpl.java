package com.csmaxwell.sps.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.mapper.SpsPermissionMapper;
import com.csmaxwell.sps.base.service.SpsPermissionService;
import com.csmaxwell.sps.base.service.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Service
public class SpsPermissionServiceImpl extends ServiceImpl<SpsPermissionMapper, SpsPermission> implements SpsPermissionService {
    @Autowired
    private UserCacheService userCacheService;

    @Override
    public boolean create(SpsPermission spsPermission) {
        spsPermission.setCreateTime(new Date());
        return save(spsPermission);
    }

    @Override
    public boolean update(Long id, SpsPermission spsPermission) {
        spsPermission.setId(id);
        boolean success = updateById(spsPermission);
        userCacheService.delPermissionListByPermission(id);
        return success;
    }

    @Override
    public boolean delete(Long id) {
        boolean success = removeById(id);
        userCacheService.delPermissionListByPermission(id);
        return success;
    }

    @Override
    public Page<SpsPermission> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize,
                                    Integer pageNum) {
        Page<SpsPermission> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SpsPermission> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SpsPermission> lambda = wrapper.lambda();
        if (categoryId != null) {
            lambda.eq(SpsPermission::getCategoryId, categoryId);
        }
        if (StrUtil.isNotEmpty(nameKeyword)) {
            lambda.like(SpsPermission::getName, nameKeyword);
        }
        if (StrUtil.isNotEmpty(urlKeyword)) {
            lambda.like(SpsPermission::getPermission, urlKeyword);
        }
        return page(page, wrapper);
    }
}
