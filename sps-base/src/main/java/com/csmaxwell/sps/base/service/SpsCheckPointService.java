package com.csmaxwell.sps.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csmaxwell.sps.base.domain.SpsCheckPoint;

/**
 * <p>
 * 巡查点表 服务类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
public interface SpsCheckPointService extends IService<SpsCheckPoint> {

    boolean create(SpsCheckPoint checkPoint);

    Page<SpsCheckPoint> list(String keyword, Integer pageSize, Integer pageNum);
}
