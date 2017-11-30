package com.rainwen.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * MyBatis http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/index.html
 * Created by rain.wen on 2017/07/19.
 */
@SpringBootApplication
@MapperScan("com.rainwen.mybatis.mapper")
@PropertySource("classpath:application.properties")
@ImportResource("classpath:spring-datasource.xml")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
