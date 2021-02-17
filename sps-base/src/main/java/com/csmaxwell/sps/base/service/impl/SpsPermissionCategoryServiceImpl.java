package com.csmaxwell.sps.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csmaxwell.sps.base.domain.SpsPermissionCategory;
import com.csmaxwell.sps.base.mapper.SpsPermissionCategoryMapper;
import com.csmaxwell.sps.base.service.SpsPermissionCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 权限分类表 服务实现类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Service
public class SpsPermissionCategoryServiceImpl extends ServiceImpl<SpsPermissionCategoryMapper, SpsPermissionCategory> implements SpsPermissionCategoryService {
    @Override
    public boolean create(SpsPermissionCategory spsPermissionCategory) {
        spsPermissionCategory.setCreateTime(new Date());
        return save(spsPermissionCategory);
    }

    @Override
    public List<SpsPermissionCategory> listAll() {
        QueryWrapper<SpsPermissionCategory> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(SpsPermissionCategory::getSort);
        return list(wrapper);
    }
}
