package com.csmaxwell.sps.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csmaxwell.sps.base.domain.SpsCheckPoint;
import com.csmaxwell.sps.base.mapper.SpsCheckPointMapper;
import com.csmaxwell.sps.base.service.SpsCheckPointService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 巡查点表 服务实现类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Service
public class SpsCheckPointServiceImpl extends ServiceImpl<SpsCheckPointMapper, SpsCheckPoint> implements SpsCheckPointService {

    @Override
    public boolean create(SpsCheckPoint checkPoint) {
        checkPoint.setCreateTime(new Date());
        return save(checkPoint);
    }

    @Override
    public Page<SpsCheckPoint> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<SpsCheckPoint> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SpsCheckPoint> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SpsCheckPoint> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(SpsCheckPoint::getName, keyword);
        }
        return page(page, wrapper);
    }
}
