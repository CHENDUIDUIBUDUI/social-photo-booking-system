package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.AdminUser;
import com.socialshoot.admin.service.AdminUserService;
import com.socialshoot.admin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String username = params.get("username");
        String password = params.get("password");
        String ip = request.getRemoteAddr();
        AdminUser adminUser = adminUserService.login(username, password, ip);
        if (adminUser != null) {
            // 生成 JWT token
            String token = jwtUtil.generateToken(adminUser.getId(), adminUser.getUsername(), adminUser.getRole());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", adminUser);
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "登录成功");
        } else {
            result.put("code", 401);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        AdminUser adminUser = adminUserService.getById(id);
        if (adminUser != null) {
            result.put("code", 200);
            result.put("data", adminUser);
        } else {
            result.put("code", 404);
            result.put("message", "用户不存在");
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<AdminUser> adminUsers = adminUserService.list();
        result.put("code", 200);
        result.put("data", adminUsers);
        return result;
    }

    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody AdminUser adminUser) {
        Map<String, Object> result = new HashMap<>();
        boolean success = adminUserService.create(adminUser);
        if (success) {
            result.put("code", 200);
            result.put("message", "创建成功");
        } else {
            result.put("code", 400);
            result.put("message", "用户名已存在");
        }
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody AdminUser adminUser) {
        Map<String, Object> result = new HashMap<>();
        boolean success = adminUserService.update(adminUser);
        if (success) {
            result.put("code", 200);
            result.put("message", "更新成功");
        } else {
            result.put("code", 400);
            result.put("message", "更新失败");
        }
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = adminUserService.delete(id);
        if (success) {
            result.put("code", 200);
            result.put("message", "删除成功");
        } else {
            result.put("code", 400);
            result.put("message", "删除失败");
        }
        return result;
    }
}