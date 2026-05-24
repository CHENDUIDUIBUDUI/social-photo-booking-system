package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.User;
import com.socialshoot.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photographer")
public class ApiPhotographerController {

    @Autowired
    private UserService userService;

    // 获取摄影师信息
    @GetMapping("/info")
    public Map<String, Object> getPhotographerInfo(@RequestParam Long id) {
        return getPhotographerByUserId(id);
    }
    
    // 根据用户ID获取摄影师信息（适配小程序调用 /api/photographer/user）
    @GetMapping("/user")
    public Map<String, Object> getPhotographerByUserId(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.getById(userId);
            if (user != null) {
                // 构建摄影师信息
                Map<String, Object> photographerInfo = new HashMap<>();
                photographerInfo.put("id", user.getId());
                photographerInfo.put("name", user.getNickname());
                photographerInfo.put("avatar", user.getAvatar());
                photographerInfo.put("phone", user.getPhone());
                photographerInfo.put("rating", 4.8); // 模拟评分
                photographerInfo.put("reviews", 120); // 模拟评价数
                photographerInfo.put("works", 50); // 模拟作品数
                
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", photographerInfo);
            } else {
                result.put("code", 404);
                result.put("message", "摄影师不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 搜索摄影师
    @GetMapping("/search")
    public Map<String, Object> searchPhotographer(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.search(keyword, page, pageSize);
            
            List<Map<String, Object>> photographers = new ArrayList<>();
            for (User user : users) {
                Map<String, Object> photographerInfo = new HashMap<>();
                photographerInfo.put("id", user.getId());
                photographerInfo.put("name", user.getNickname());
                photographerInfo.put("avatar", user.getAvatar());
                photographerInfo.put("specialty", user.getSpecialty());
                photographers.add(photographerInfo);
            }
            
            result.put("code", 200);
            result.put("message", "搜索成功");
            result.put("data", photographers);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }

}
