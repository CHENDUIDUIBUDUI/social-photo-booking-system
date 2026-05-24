package com.photo.booking.controller;

import com.photo.booking.service.CollectionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/collection")
public class CollectionController {

    @Resource
    private CollectionService collectionService;

    @PostMapping("/toggle")
    public Map<String, Object> toggleCollection(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = Long.parseLong(params.get("userId").toString());
            Long contentId = Long.parseLong(params.get("contentId").toString());
            boolean collected = Boolean.parseBoolean(params.get("collected").toString());
            
            boolean success;
            if (collected) {
                success = collectionService.addCollection(userId, contentId);
            } else {
                success = collectionService.removeCollection(userId, contentId);
            }
            if (success) {
                int collectionCount = collectionService.getCollectionCount(contentId);
                result.put("code", 200);
                result.put("message", "操作成功");
                result.put("data", Map.of("collectionCount", collectionCount, "collected", collected));
            } else {
                result.put("code", 400);
                result.put("message", "操作失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/status")
    public Map<String, Object> getCollectionStatus(@RequestParam Long userId, @RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean collected = collectionService.isCollected(userId, contentId);
            int collectionCount = collectionService.getCollectionCount(contentId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", Map.of("collected", collected, "collectionCount", collectionCount));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> getCollections(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> collections = collectionService.getCollectionsByUserId(userId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", collections);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count")
    public Map<String, Object> getCollectionCount(@RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = collectionService.getCollectionCount(contentId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", Map.of("count", count));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
}
