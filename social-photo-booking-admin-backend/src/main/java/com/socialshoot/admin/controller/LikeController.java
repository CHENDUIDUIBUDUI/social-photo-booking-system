package com.socialshoot.admin.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    // 获取内容点赞状态
    @GetMapping("/content/status")
    public Map<String, Object> getContentLikeStatus(
            @RequestParam Long userId,
            @RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (userId == null || contentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("isLiked", false);
            data.put("likeCount", 0);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}