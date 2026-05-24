package com.socialshoot.admin.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    // 添加评价
    @PostMapping("/create")
    public Map<String, Object> createRating(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }

            // 这里可以实现添加评价的逻辑
            // 1. 检查订单是否存在且已完成
            // 2. 检查是否已经评价过
            // 3. 添加评价记录
            // 4. 更新用户信用积分

            result.put("code", 200);
            result.put("message", "评价成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "评价失败：" + e.getMessage());
        }
        return result;
    }

    // 获取评价列表
    @GetMapping("/list")
    public Map<String, Object> getRatingList(
            @RequestHeader("Authorization") String token,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long currentUserId = getUserIdFromToken(token);
            if (currentUserId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }

            // 这里可以实现获取评价列表的逻辑
            // 1. 检查权限
            // 2. 查询评价记录
            // 3. 分页处理

            Map<String, Object> data = new HashMap<>();
            data.put("list", new java.util.ArrayList<>());
            data.put("total", 0);
            data.put("page", page);
            data.put("pageSize", pageSize);

            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取用户信用积分
    @GetMapping("/credit")
    public Map<String, Object> getUserCredit(
            @RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }

            // 这里可以实现获取用户信用积分的逻辑
            // 1. 查询用户信用积分
            // 2. 查询信用积分记录

            Map<String, Object> data = new HashMap<>();
            data.put("totalCredit", 100);
            data.put("records", new java.util.ArrayList<>());

            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    private Long getUserIdFromToken(String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            String[] parts = tokenValue.split("\\.");
            if (parts.length == 3) {
                String payload = parts[1];
                String decoded = new String(java.util.Base64.getUrlDecoder().decode(payload));
                return Long.parseLong(decoded.substring(decoded.indexOf("\"sub\":\"") + 7, decoded.indexOf("\",\"")));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
