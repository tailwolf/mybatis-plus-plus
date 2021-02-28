package com.tailwolf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.tailwolf.mybatis.core.annotation.MybatisCompleteScans;

@SpringBootApplication
@MapperScan({"com.tailwolf.mapper"})
@MybatisCompleteScans
public class StarterTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarterTestApplication.class, args);
    }
}
