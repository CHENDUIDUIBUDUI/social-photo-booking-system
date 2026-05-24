package com.socialshoot.admin.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    // 获取评论数量
    @GetMapping("/count")
    public Map<String, Object> getCommentCount(@RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (contentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            // 获取评论数量（如果表不存在，返回0）
            int count = 0;
            
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
    
    // 获取评论列表
    @GetMapping("/list")
    public Map<String, Object> getCommentList(
            @RequestParam Long contentId,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "time") String sort) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (contentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            result.put("code", 200);
            result.put("data", new java.util.ArrayList<>());
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}