package com.socialshoot.admin.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/package")
public class PackageController {

    // 获取摄影师的套餐列表
    @GetMapping("/photographer")
    public Map<String, Object> getPackagesByPhotographer(@RequestParam Long photographerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (photographerId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            // 返回空的套餐列表（如果数据库中没有套餐表）
            result.put("code", 200);
            result.put("data", new ArrayList<>());
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}