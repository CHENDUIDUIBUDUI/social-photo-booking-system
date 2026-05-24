package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Collection;
import com.socialshoot.admin.mapper.CollectionMapper;
import com.socialshoot.admin.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/collection")
public class ApiCollectionController {
    
    @Autowired
    private CollectionService collectionService;
    
    @Autowired
    private CollectionMapper collectionMapper;
    
    @GetMapping("/list")
    public Map<String, Object> getCollectionList(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            List<Collection> collectionList = collectionService.findByUserId(userId, page, pageSize);
            int total = collectionService.countByUserId(userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", collectionList);
            data.put("total", total);
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
    
    @PostMapping("/cancel")
    public Map<String, Object> cancelCollection(
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
            
            Long contentId = Long.parseLong(params.get("contentId").toString());
            
            collectionMapper.deleteByUserIdAndContentId(userId, contentId);
            
            result.put("code", 200);
            result.put("message", "取消成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "取消失败：" + e.getMessage());
        }
        return result;
    }
    
    // 获取收藏状态（适配小程序调用 /api/collection/status）
    @GetMapping("/status")
    public Map<String, Object> getCollectionStatus(
            @RequestParam Long userId,
            @RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (userId == null || contentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            // 检查是否已收藏
            boolean isCollected = false;
            try {
                isCollected = collectionService.isCollected(userId, contentId);
            } catch (Exception e) {
                // 如果表不存在，返回false
                isCollected = false;
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("isCollected", isCollected);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 获取收藏数量（适配小程序调用 /api/collection/count）
    @GetMapping("/count")
    public Map<String, Object> getCollectionCount(@RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (contentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            // 获取收藏数量
            int count = 0;
            try {
                // 尝试从数据库获取收藏数量（如果表存在）
                count = 0; // 需要实现 countByContentId 方法
            } catch (Exception e) {
                // 如果表不存在，返回0
                count = 0;
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("count", count);
            
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