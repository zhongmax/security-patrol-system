package com.csmaxwell.sps.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * S
 * Created by maxwell on 2021/2/16.
 */
@SpringBootApplication(scanBasePackages = {"com.csmaxwell.sps.base", "com.csmaxwell.sps.web"})
@MapperScan("com.csmaxwell.sps.base.mapper")
@EnableTransactionManagement
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
