package com.photo.booking.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseMigration implements CommandLineRunner {

    @Resource
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("数据库连接成功，跳过复杂迁移步骤...");
            // 简化版：只做基本检查，不执行复杂的表结构修改
            // 避免之前导致服务崩溃的问题
        } catch (SQLException e) {
            System.err.println("数据库连接失败: " + e.getMessage());
        }
    }
}