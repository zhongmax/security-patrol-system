package com.csmaxwell.sps.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * S
 * Created by maxwell on 2021/2/16.
 */
@SpringBootApplication(scanBasePackages = {"com.csmaxwell.sps.base", "com.csmaxwell.sps.wx"})
@MapperScan("com.csmaxwell.sps.base.mapper")
@EnableTransactionManagement
public class WxApplication {
    public static void main(String[] args) {
        SpringApplication.run(WxApplication.class, args);
    }
}
