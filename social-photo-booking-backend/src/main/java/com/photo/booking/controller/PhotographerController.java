package com.photo.booking.controller;

import com.alibaba.fastjson.JSON;
import com.photo.booking.entity.Content;
import com.photo.booking.entity.Photographer;
import com.photo.booking.entity.User;
import com.photo.booking.mapper.ContentMapper;
import com.photo.booking.service.PhotographerService;
import com.photo.booking.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/photographer")
public class PhotographerController {

    @Resource
    private PhotographerService photographerService;
    
    @Resource
    private ContentMapper contentMapper;
    
    @Resource
    private UserService userService;

    @GetMapping("/info")
    public Map<String, Object> getPhotographerInfo(@RequestParam(required = false) Long id, @RequestParam(required = false) Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Photographer photographer = null;
            if (id != null) {
                // 先尝试根据摄影师ID查询
                photographer = photographerService.getPhotographerById(id);
                // 如果查询不到，尝试根据用户ID查询
                if (photographer == null) {
                    photographer = photographerService.getPhotographerByUserId(id);
                }
            } else if (userId != null) {
                // 根据用户ID查询
                photographer = photographerService.getPhotographerByUserId(userId);
            }
            
            if (photographer != null) {
                // 从content表中获取该摄影师的作品
                List<Content> contents = contentMapper.selectByUserId(photographer.getUserId(), 1); // 1表示作品类型
                
                // 将作品列表转换为图片URL数组
                List<String> works = contents.stream()
                    .map(Content::getCoverImage)
                    .filter(coverImage -> coverImage != null && !coverImage.isEmpty())
                    .collect(Collectors.toList());
                
                // 如果从content表获取到了作品，更新photographer的works字段
                if (!works.isEmpty()) {
                    // 将List转换为JSON格式的字符串
                    photographer.setWorks(JSON.toJSONString(works));
                }
                
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", photographer);
            } else {
                // 如果所有查询都失败，返回更详细的错误信息
                result.put("code", 404);
                result.put("message", "摄影师不存在");
                result.put("debug", "尝试查询的ID: " + id + ", userId: " + userId);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/user")
    public Map<String, Object> getPhotographerByUserId(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Photographer photographer = photographerService.getPhotographerByUserId(userId);
            if (photographer != null) {
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", photographer);
            } else {
                result.put("code", 404);
                result.put("message", "摄影师不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/list")
    public Map<String, Object> getPhotographerList(@RequestBody(required = false) Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从请求体提取参数（兼容 body 为空）
            String region = params != null && params.get("region") != null ? params.get("region").toString() : null;
            String style = params != null && params.get("style") != null ? params.get("style").toString() : null;
            BigDecimal minPrice = params != null && params.get("minPrice") != null ? new BigDecimal(params.get("minPrice").toString()) : null;
            BigDecimal maxPrice = params != null && params.get("maxPrice") != null ? new BigDecimal(params.get("maxPrice").toString()) : null;
            Integer certified = params != null && params.get("certified") != null ? Integer.valueOf(params.get("certified").toString()) : null;
            String sort = params != null && params.get("sort") != null ? params.get("sort").toString() : null;

            // 获取所有摄影师列表
            List<Photographer> photographers = photographerService.getAllPhotographers();
            
            // 按风格筛选
            if (style != null && !style.isEmpty()) {
                String[] styles = style.split(",");
                photographers = photographers.stream().filter(p -> {
                    if (p.getStyles() == null) return false;
                    for (String s : styles) {
                        if (p.getStyles().contains(s)) {
                            return true;
                        }
                    }
                    return false;
                }).collect(java.util.stream.Collectors.toList());
            }
            
            // 按区域筛选
            if (region != null && !region.isEmpty()) {
                photographers = photographers.stream().filter(p -> {
                    return p.getRegion() != null && p.getRegion().equals(region);
                }).collect(java.util.stream.Collectors.toList());
            }
            
            // 按价格筛选
            if (minPrice != null && maxPrice != null) {
                photographers = photographers.stream().filter(p -> {
                    return p.getPrice() != null && p.getPrice().compareTo(minPrice) >= 0 && p.getPrice().compareTo(maxPrice) <= 0;
                }).collect(java.util.stream.Collectors.toList());
            } else if (maxPrice != null) {
                photographers = photographers.stream().filter(p -> {
                    return p.getPrice() != null && p.getPrice().compareTo(maxPrice) <= 0;
                }).collect(java.util.stream.Collectors.toList());
            } else if (minPrice != null) {
                photographers = photographers.stream().filter(p -> {
                    return p.getPrice() != null && p.getPrice().compareTo(minPrice) >= 0;
                }).collect(java.util.stream.Collectors.toList());
            }
            
            // 按认证筛选
            if (certified != null) {
                photographers = photographers.stream().filter(p -> {
                    return p.getCertified() != null && p.getCertified().equals(certified);
                }).collect(java.util.stream.Collectors.toList());
            }
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", photographers);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create")
    public Map<String, Object> createPhotographer(@RequestBody Photographer photographer) {
        Map<String, Object> result = new HashMap<>();
        try {
            Photographer created = photographerService.createPhotographer(photographer);
            result.put("code", 200);
            result.put("message", "创建成功");
            result.put("data", created);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/apply")
    public Map<String, Object> applyPhotographer(@RequestBody Photographer photographer) {
        Map<String, Object> result = new HashMap<>();
        try {
            photographer.setStatus(0);
            photographer.setCertified(0);
            photographer.setOrders(0);
            photographer.setRating(java.math.BigDecimal.ZERO);
            Photographer created = photographerService.createPhotographer(photographer);
            result.put("code", 200);
            result.put("message", "申请提交成功");
            result.put("data", created);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> updatePhotographer(@RequestBody Photographer photographer) {
        Map<String, Object> result = new HashMap<>();
        try {
            Photographer updated = photographerService.updatePhotographer(photographer);
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/orders")
    public Map<String, Object> updatePhotographerOrders(@RequestParam Long id, @RequestParam Integer orders) {
        Map<String, Object> result = new HashMap<>();
        try {
            photographerService.updatePhotographerOrders(id, orders);
            result.put("code", 200);
            result.put("message", "更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/rating")
    public Map<String, Object> updatePhotographerRating(@RequestParam Long id, @RequestParam BigDecimal rating) {
        Map<String, Object> result = new HashMap<>();
        try {
            photographerService.updatePhotographerRating(id, rating);
            result.put("code", 200);
            result.put("message", "更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/status")
    public Map<String, Object> updatePhotographerStatus(@RequestParam Long id, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            photographerService.updatePhotographerStatus(id, status);
            result.put("code", 200);
            result.put("message", "更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/hot")
    public Map<String, Object> getHotPhotographers(@RequestParam(required = false, defaultValue = "5") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Photographer> photographers = photographerService.getHotPhotographers(limit);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", photographers);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/debug")
    public Map<String, Object> debug(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查用户信息
            result.put("userId", userId);
            
            // 检查摄影师信息
            Photographer photographer = photographerService.getPhotographerByUserId(userId);
            if (photographer != null) {
                result.put("photographer", photographer);
                result.put("photographerExists", true);
            } else {
                result.put("photographerExists", false);
            }
            
            result.put("code", 200);
            result.put("message", "success");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/sync")
    public Map<String, Object> syncPhotographer(@RequestParam String phone) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 根据手机号查询用户
            User user = userService.getUserByPhone(phone);
            if (user == null) {
                result.put("code", 404);
                result.put("message", "用户不存在");
                return result;
            }
            
            // 检查用户是否是摄影师角色
            if (user.getRole() == null || user.getRole() != 1) {
                result.put("code", 400);
                result.put("message", "用户不是摄影师角色");
                return result;
            }
            
            // 检查摄影师记录是否已存在
            Photographer existing = photographerService.getPhotographerByUserId(user.getId());
            if (existing != null) {
                result.put("code", 400);
                result.put("message", "摄影师记录已存在");
                result.put("data", existing);
                return result;
            }
            
            // 创建摄影师记录
            Photographer photographer = new Photographer();
            photographer.setUserId(user.getId());
            photographer.setName(user.getNickname());
            photographer.setAvatar(user.getAvatar());
            photographer.setCertified(1);
            photographer.setCertifiedTime(new Date());
            photographer.setOrders(0);
            photographer.setRating(new BigDecimal("0.0"));
            photographer.setPrice(new BigDecimal("0.0"));
            photographer.setStatus(1);
            photographer.setCreateTime(new Date());
            photographer.setUpdateTime(new Date());
            
            photographerService.createPhotographer(photographer);
            
            result.put("code", 200);
            result.put("message", "同步成功");
            result.put("data", photographer);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
}