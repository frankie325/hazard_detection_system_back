package com.expressway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.expressway.mapper")
public class ExpresswayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpresswayServerApplication.class, args);
    }

}
