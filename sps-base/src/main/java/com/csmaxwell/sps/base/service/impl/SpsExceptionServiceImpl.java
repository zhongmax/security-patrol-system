package com.csmaxwell.sps.base.service.impl;

import com.csmaxwell.sps.base.domain.SpsException;
import com.csmaxwell.sps.base.mapper.SpsExceptionMapper;
import com.csmaxwell.sps.base.service.SpsExceptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 检查点异常表 服务实现类
 * </p>
 *
 * @author maxwell
 * @since 2021-02-16
 */
@Service
public class SpsExceptionServiceImpl extends ServiceImpl<SpsExceptionMapper, SpsException> implements SpsExceptionService {

}
