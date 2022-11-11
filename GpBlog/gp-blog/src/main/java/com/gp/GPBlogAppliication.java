package com.gp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.gp.mapper")
@EnableScheduling
@EnableSwagger2
public class GPBlogAppliication {
    public static void main(String[] args) {
        SpringApplication.run(GPBlogAppliication.class, args);

    }
}



