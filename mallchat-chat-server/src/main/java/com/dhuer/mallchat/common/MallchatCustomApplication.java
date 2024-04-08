package com.dhuer.mallchat.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jintao Li
 * @date 2024/04/01
 */
@SpringBootApplication(scanBasePackages = {"com.dhuer.mallchat"})
@MapperScan({"com.dhuer.mallchat.common.**.mapper"})
public class MallchatCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallchatCustomApplication.class,args);
    }
}