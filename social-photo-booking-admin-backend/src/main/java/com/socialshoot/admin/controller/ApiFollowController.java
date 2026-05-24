package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Follow;
import com.socialshoot.admin.mapper.FollowMapper;
import com.socialshoot.admin.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
public class ApiFollowController {
    
    @Autowired
    private FollowService followService;
    
    @Autowired
    private FollowMapper followMapper;
    
    @GetMapping("/list")
    public Map<String, Object> getFollowList(
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
            
            List<Follow> followList = followService.findByUserId(userId, page, pageSize);
            int total = followService.countByUserId(userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", followList);
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
    public Map<String, Object> cancelFollow(
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
            
            Long followUserId = Long.parseLong(params.get("followUserId").toString());
            
            followMapper.deleteByUserIdAndFollowUserId(userId, followUserId);
            
            result.put("code", 200);
            result.put("message", "取消成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "取消失败：" + e.getMessage());
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