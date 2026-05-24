package com.socialshoot.admin.controller;

import com.github.pagehelper.PageInfo;
import com.socialshoot.admin.entity.User;
import com.socialshoot.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Map<String, Object> getPageList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String role) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageInfo<User> pageInfo = userService.getPageList(pageNum, pageSize, nickname, phone, role);
            result.put("code", 200);
            result.put("data", pageInfo);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.getById(id);
            if (user != null) {
                result.put("code", 200);
                result.put("data", user);
            } else {
                result.put("code", 404);
                result.put("message", "用户不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/updateStatus")
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long id = Long.parseLong(params.get("id").toString());
            Integer status = Integer.parseInt(params.get("status").toString());
            boolean success = userService.updateStatus(id, status);
            if (success) {
                result.put("code", 200);
                result.put("message", "状态更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "状态更新失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/updateRole")
    public Map<String, Object> updateRole(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long id = Long.parseLong(params.get("id").toString());
            Integer roleId = Integer.parseInt(params.get("roleId").toString());
            boolean success = userService.updateRole(id, roleId);
            if (success) {
                result.put("code", 200);
                result.put("message", "角色更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "角色更新失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/photographer/hot")
    public Map<String, Object> getHotPhotographers(@RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getHotPhotographers(limit);
            List<Map<String, Object>> photographers = new ArrayList<>();
            for (User user : users) {
                Map<String, Object> photographer = new HashMap<>();
                photographer.put("id", user.getId());
                photographer.put("name", user.getNickname()); // 返回name字段适配前端
                photographer.put("avatar", user.getAvatar());
                photographers.add(photographer);
            }
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", photographers);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/photographer/list")
    public Map<String, Object> getPhotographerList(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String style,
            @RequestParam(required = false) Integer maxPrice) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getPhotographerList(region, style, maxPrice);
            List<Map<String, Object>> photographers = new ArrayList<>();
            for (User user : users) {
                Map<String, Object> photographer = new HashMap<>();
                photographer.put("id", user.getId());
                photographer.put("name", user.getNickname());
                photographer.put("avatar", user.getAvatar());
                photographer.put("certified", user.getRoleId() != null && user.getRoleId() == 1 ? 1 : 0);
                photographer.put("orders", 0);
                photographer.put("rating", 5.0);
                photographer.put("styles", "人像,风景,街拍");
                photographer.put("region", "北京");
                photographer.put("price", 500);
                photographers.add(photographer);
            }
            if (photographers.isEmpty()) {
                String[] names = {"张摄影师", "李摄影师", "王摄影师", "刘摄影师", "陈摄影师"};
                for (int i = 0; i < names.length; i++) {
                    Map<String, Object> photographer = new HashMap<>();
                    photographer.put("id", i + 1);
                    photographer.put("name", names[i]);
                    photographer.put("avatar", "https://api.dicebear.com/7.x/personas/svg?seed=" + names[i] + "&size=200");
                    photographer.put("certified", 1);
                    photographer.put("orders", 100 + i * 50);
                    photographer.put("rating", 4.8 + i * 0.1);
                    photographer.put("styles", "人像,风景,街拍");
                    photographer.put("region", "北京");
                    photographer.put("price", 500 + i * 100);
                    photographers.add(photographer);
                }
            }
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", photographers);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}
