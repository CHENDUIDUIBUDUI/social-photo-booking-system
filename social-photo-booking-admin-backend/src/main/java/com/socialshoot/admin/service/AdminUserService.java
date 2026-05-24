package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.AdminUser;
import java.util.List;

public interface AdminUserService {
    AdminUser login(String username, String password, String ip);
    AdminUser getById(Long id);
    List<AdminUser> list();
    boolean create(AdminUser adminUser);
    boolean update(AdminUser adminUser);
    boolean delete(Long id);
}