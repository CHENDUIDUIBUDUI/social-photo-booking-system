package com.photo.booking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan(basePackages = "com.photo.booking.mapper")
@ComponentScan(basePackages = "com.photo.booking")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public CommandLineRunner initDatabase(@NonNull DataSource dataSource) {
        return args -> {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            
            // 检查user表是否存在password字段
            try {
                jdbcTemplate.queryForObject("SELECT password FROM user LIMIT 1", String.class);
                System.out.println("password列已存在，无需添加。");
            } catch (Exception e) {
                // password列不存在，添加该列
                try {
                    jdbcTemplate.execute("ALTER TABLE user ADD COLUMN password VARCHAR(255) COMMENT '密码'");
                    System.out.println("成功添加password列。");
                } catch (Exception ex) {
                    System.out.println("添加password列失败: " + ex.getMessage());
                }
            }
        };
    }
}
