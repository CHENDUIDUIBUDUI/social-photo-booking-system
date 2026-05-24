package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Content;
import com.socialshoot.admin.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content/admin")
public class ContentController {

    @Autowired
    private ContentService contentService;

    // 获取内容列表（支持搜索）
    @GetMapping("/list")
    public Map<String, Object> getContentList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 使用search方法支持搜索参数筛选
            List<Content> list = contentService.search(id, title, type, status, pageNum, pageSize);
            int total = contentService.countSearch(id, title, type, status);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", list);
            data.put("total", total);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取内容详情
    @GetMapping("/detail/{id}")
    public Map<String, Object> getContentDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Content content = contentService.findById(id);
            if (content != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", content);
            } else {
                result.put("code", 404);
                result.put("message", "内容不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取内容标签
    @GetMapping("/tags/{id}")
    public Map<String, Object> getContentTags(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 这里简化处理，实际应该从数据库获取标签
            List<String> tags = List.of("摄影", "人像", "街拍");
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", tags);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取内容图片
    @GetMapping("/images/{id}")
    public Map<String, Object> getContentImages(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 这里简化处理，实际应该从数据库获取图片
            List<Map<String, String>> images = List.of(
                    Map.of("imageUrl", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=750&h=300&fit=crop"),
                    Map.of("imageUrl", "https://images.unsplash.com/photo-1519741497674-611481863552?w=750&h=300&fit=crop")
            );
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", images);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 审核通过
    @PostMapping("/approve")
    public Map<String, Object> approveContent(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long id = Long.valueOf(params.get("id").toString());
            contentService.approveContent(id);
            result.put("code", 200);
            result.put("message", "审核通过成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "审核通过失败：" + e.getMessage());
        }
        return result;
    }

    // 审核拒绝
    @PostMapping("/reject")
    public Map<String, Object> rejectContent(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long id = Long.valueOf(params.get("id").toString());
            String reason = (String) params.get("reason");
            contentService.rejectContent(id, reason);
            result.put("code", 200);
            result.put("message", "审核拒绝成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "审核拒绝失败：" + e.getMessage());
        }
        return result;
    }
}
