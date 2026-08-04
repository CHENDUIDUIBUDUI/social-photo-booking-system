package com.photo.booking.controller;

import com.photo.booking.entity.Content;
import com.photo.booking.entity.Photographer;
import com.photo.booking.service.ContentService;
import com.photo.booking.service.PhotographerService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Resource
    private ContentService contentService;

    @Resource
    private PhotographerService photographerService;

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
    // 获取作品列表
    @PostMapping("/list")
    public Map<String, Object> getContentList(@RequestBody(required = false) Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从请求体提取参数（兼容 body 为空）
            int page = params != null && params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            int pageSize = params != null && params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String tags = params != null && params.get("tags") != null ? params.get("tags").toString() : null;
            String city = params != null && params.get("city") != null ? params.get("city").toString() : null;
            String category = params != null && params.get("category") != null ? params.get("category").toString() : null;

            // 从 photographer 表查询全部摄影师
            List<Photographer> allPhotographers = photographerService.getAllPhotographers();

            // 仅保留正常状态（status=1）的摄影师
            List<Photographer> filtered = allPhotographers.stream()
                    .filter(p -> p.getStatus() != null && p.getStatus() == 1)
                    .collect(Collectors.toList());

            // 按城市/区域筛选（city 对应 region）
            if (city != null && !city.isEmpty()) {
                filtered = filtered.stream()
                        .filter(p -> p.getRegion() != null && p.getRegion().contains(city))
                        .collect(Collectors.toList());
            }

            // 按风格筛选（tags/category 对应 styles）
            String styleKey = (tags != null && !tags.isEmpty()) ? tags : category;
            if (styleKey != null && !styleKey.isEmpty()) {
                filtered = filtered.stream()
                        .filter(p -> p.getStyles() != null && p.getStyles().contains(styleKey))
                        .collect(Collectors.toList());
            }

            // 内存分页
            int total = filtered.size();
            int fromIndex = (page - 1) * pageSize;
            List<Photographer> pageList;
            if (fromIndex >= total) {
                pageList = new ArrayList<>();
            } else {
                int toIndex = Math.min(fromIndex + pageSize, total);
                pageList = new ArrayList<>(filtered.subList(fromIndex, toIndex));
            }

            // 控制台打印查询结果，便于调试
            System.out.println("===== /api/content/list（查询 photographer 表）请求参数 =====");
            System.out.println("page=" + page + ", pageSize=" + pageSize + ", city=" + city + ", tags=" + tags + ", category=" + category);
            System.out.println("===== 数据库查询结果（共 " + total + " 条，本页返回 " + pageList.size() + " 条）=====");
            pageList.forEach(System.out::println);

            Map<String, Object> data = new HashMap<>();
            data.put("list", pageList);
            data.put("total", total);
            data.put("page", page);
            data.put("pageSize", pageSize);
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
