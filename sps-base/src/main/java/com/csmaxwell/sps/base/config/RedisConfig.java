package com.csmaxwell.sps.base.config;

import com.csmaxwell.sps.base.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 * Created by maxwell on 2021/2/16.
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {
}
