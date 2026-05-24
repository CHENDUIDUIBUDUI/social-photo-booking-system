package com.photo.booking.controller;

import com.photo.booking.entity.Content;
import com.photo.booking.service.ContentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Resource
    private ContentService contentService;

    @PostMapping("/create")
    public Map<String, Object> createContent(@RequestBody Content content) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 从SecurityContext中获取用户ID
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && !"anonymousUser".equals(principal.toString())) {
                try {
                    Long userId = Long.parseLong(principal.toString());
                    content.setUserId(userId);
                } catch (NumberFormatException e) {
                    result.put("code", 401);
                    result.put("message", "用户未登录");
                    return result;
                }
            } else {
                result.put("code", 401);
                result.put("message", "用户未登录");
                return result;
            }
            
            // 设置默认值
            content.setStatus(0); // 0:待审核
            content.setViewCount(0);
            content.setLikeCount(0);
            content.setCommentCount(0);
            content.setCreateTime(new Date());
            content.setUpdateTime(new Date());
            
            Content createdContent = contentService.createContent(content);
            
            result.put("code", 200);
            result.put("message", "发布成功");
            result.put("data", createdContent);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "发布失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> getContentInfo(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Content content = contentService.getContentById(id);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", content);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public Content updateContent(@RequestBody Content content) {
        return contentService.updateContent(content);
    }

    @PutMapping("/status")
    public void updateContentStatus(@RequestParam Long id, @RequestParam Integer status) {
        contentService.updateContentStatus(id, status);
    }

    @GetMapping("/list")
    public Map<String, Object> getContentList(
            @RequestParam(required = false) Integer type, 
            @RequestParam(required = false) Integer status, 
            @RequestParam(required = false) String tags, 
            @RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 计算偏移量
            int offset = (page - 1) * pageSize;
            List<Content> contentList = contentService.getContentList(type, status, tags, city, page, pageSize, offset);
            Map<String, Object> data = new HashMap<>();
            data.put("list", contentList);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/user")
    public List<Content> getContentByUserId(@RequestParam Long userId, @RequestParam(required = false) Integer type) {
        return contentService.getContentByUserId(userId, type);
    }

    @GetMapping("/pending")
    public List<Content> getPendingReview() {
        return contentService.getPendingReview();
    }
    
    @GetMapping("/hot")
    public List<Content> getHotContents(@RequestParam(defaultValue = "10") Integer limit) {
        return contentService.getHotContents(limit);
    }
}
