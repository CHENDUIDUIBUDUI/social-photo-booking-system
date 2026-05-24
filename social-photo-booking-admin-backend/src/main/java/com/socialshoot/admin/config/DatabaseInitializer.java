package com.socialshoot.admin.config;

import com.socialshoot.admin.entity.AdminUser;
import com.socialshoot.admin.mapper.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private AdminUserMapper adminUserMapper;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有管理员账号
        if (adminUserMapper.selectByUsername("admin") == null) {
            // 创建超级管理员账号
            AdminUser adminUser = new AdminUser();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setRealName("超级管理员");
            adminUser.setAvatar("https://via.placeholder.com/150");
            adminUser.setRole(1); // 超级管理员
            adminUser.setStatus(1); // 启用状态
            adminUserMapper.insert(adminUser);
            System.out.println("超级管理员账号初始化成功");
        }

        // 创建高级管理员账号
        if (adminUserMapper.selectByUsername("senior") == null) {
            AdminUser seniorUser = new AdminUser();
            seniorUser.setUsername("senior");
            seniorUser.setPassword(passwordEncoder.encode("admin"));
            seniorUser.setRealName("高级管理员");
            seniorUser.setAvatar("https://via.placeholder.com/150");
            seniorUser.setRole(2); // 高级管理员
            seniorUser.setStatus(1); // 启用状态
            adminUserMapper.insert(seniorUser);
            System.out.println("高级管理员账号初始化成功");
        }

        // 创建普通管理员账号
        if (adminUserMapper.selectByUsername("normal") == null) {
            AdminUser normalUser = new AdminUser();
            normalUser.setUsername("normal");
            normalUser.setPassword(passwordEncoder.encode("admin"));
            normalUser.setRealName("普通管理员");
            normalUser.setAvatar("https://via.placeholder.com/150");
            normalUser.setRole(3); // 普通管理员
            normalUser.setStatus(1); // 启用状态
            adminUserMapper.insert(normalUser);
            System.out.println("普通管理员账号初始化成功");
        }
    }
}
