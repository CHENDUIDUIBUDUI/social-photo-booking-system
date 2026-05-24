package com.photo.booking.controller;

import com.photo.booking.entity.Package;
import com.photo.booking.service.PackageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/package")
public class PackageController {
    
    @Resource
    private PackageService packageService;
    
    // 获取摄影师套餐列表
    @GetMapping("/list")
    public Map<String, Object> getPackageList(@RequestParam Long photographerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Package> packages = packageService.getPackagesByPhotographerId(photographerId);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", packages);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
    
    // 获取套餐详情
    @GetMapping("/info")
    public Map<String, Object> getPackageInfo(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Package pack = packageService.getPackageById(id);
            if (pack != null) {
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", pack);
            } else {
                result.put("code", 404);
                result.put("message", "套餐不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
    
    // 根据摄影师ID获取套餐列表
    @GetMapping("/photographer")
    public Map<String, Object> getPackagesByPhotographerId(@RequestParam Long photographerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Package> packages = packageService.getPackagesByPhotographerId(photographerId);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", packages);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
    
    // 创建套餐
    @PostMapping("/create")
    public Map<String, Object> createPackage(@RequestBody Package pack) {
        Map<String, Object> result = new HashMap<>();
        try {
            Package created = packageService.createPackage(pack);
            result.put("code", 200);
            result.put("message", "创建成功");
            result.put("data", created);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
    
    // 更新套餐
    @PutMapping("/update")
    public Map<String, Object> updatePackage(@RequestBody Package pack) {
        Map<String, Object> result = new HashMap<>();
        try {
            Package updated = packageService.updatePackage(pack);
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
    
    // 删除套餐
    @DeleteMapping("/delete")
    public Map<String, Object> deletePackage(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            packageService.deletePackage(id);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
}