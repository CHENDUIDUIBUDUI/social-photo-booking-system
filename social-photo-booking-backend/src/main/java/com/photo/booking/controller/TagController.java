package com.photo.booking.controller;

import com.photo.booking.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/list")
    public Map<String, Object> getCategoryList(@RequestParam(required = false) Integer type) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (type != null) {
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", tagService.getTagsByType(type));
            } else {
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", tagService.getAllTags());
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
}