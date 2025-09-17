package com.baitabei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 白塔杯文化创意大赛后端应用启动类
 * 
 * @author MiniMax Agent
 */
@SpringBootApplication
@MapperScan("com.baitabei.mapper")
public class CompetitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompetitionApplication.class, args);
        System.out.println("=================================");
        System.out.println("白塔杯大赛后端服务启动成功！");
        System.out.println("API文档地址: http://localhost:8080/doc.html");
        System.out.println("=================================");
    }
}
