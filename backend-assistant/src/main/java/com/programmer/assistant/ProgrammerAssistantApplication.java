package com.programmer.assistant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序员工作助手主应用程序
 */
@SpringBootApplication
@MapperScan("com.programmer.assistant.mapper")
public class ProgrammerAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgrammerAssistantApplication.class, args);
    }
}
