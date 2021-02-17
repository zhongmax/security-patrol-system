package com.csmaxwell.sps;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * S
 * Created by maxwell on 2021/2/16.
 */
@SpringBootApplication(scanBasePackages = {"com.csmaxwell.sps"})
@MapperScan("com.csmaxwell.sps.base.mapper")
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
