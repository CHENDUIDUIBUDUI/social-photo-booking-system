package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.ModelCard;
import com.socialshoot.admin.service.ModelCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/model")
public class ApiModelCardController {

    @Autowired
    private ModelCardService modelCardService;

    // 创建/更新模特卡
    @PostMapping("/card/save")
    public Map<String, Object> saveModelCard(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            ModelCard modelCard = modelCardService.findByUserId(userId);
            
            if (modelCard == null) {
                modelCard = new ModelCard();
                modelCard.setUserId(userId);
                modelCard.setStatus(0); // 默认为待审核
                modelCard.setCreateTime(LocalDateTime.now());
            }
            
            modelCard.setName((String) params.get("name"));
            modelCard.setHeight(Integer.valueOf(params.get("height").toString()));
            modelCard.setWeight(Integer.valueOf(params.get("weight").toString()));
            modelCard.setStyles((String) params.get("styles"));
            modelCard.setIsPaid(Integer.valueOf(params.get("isPaid").toString()));
            modelCard.setAvailability((String) params.get("availability"));
            modelCard.setPortfolio((String) params.get("portfolio"));
            modelCard.setIntroduction((String) params.get("introduction"));
            modelCard.setUpdateTime(LocalDateTime.now());

            modelCardService.save(modelCard);

            result.put("code", 200);
            result.put("message", "保存成功");
            result.put("data", modelCard);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "保存失败：" + e.getMessage());
        }
        return result;
    }

    // 获取模特卡详情
    @GetMapping("/card/detail")
    public Map<String, Object> getModelCardDetail(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            ModelCard modelCard = modelCardService.findByUserId(userId);
            if (modelCard != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", modelCard);
            } else {
                result.put("code", 404);
                result.put("message", "模特卡不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取模特列表
    @GetMapping("/list")
    public Map<String, Object> getModelList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String style) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ModelCard> list = modelCardService.findAll(page, pageSize);
            
            // 按风格筛选
            if (style != null && !style.isEmpty()) {
                list = list.stream().filter(card -> {
                    try {
                        return card.getStyles() != null && card.getStyles().contains(style);
                    } catch (Exception e) {
                        return false;
                    }
                }).toList();
            }
            
            int total = modelCardService.countAll();
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", list);
            data.put("total", total);
            data.put("page", page);
            data.put("pageSize", pageSize);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 审核模特卡
    @PostMapping("/card/audit")
    public Map<String, Object> auditModelCard(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long id = Long.valueOf(params.get("id").toString());
            Integer status = Integer.valueOf(params.get("status").toString());
            Long auditorId = 1L; // 暂时使用默认管理员ID
            
            if (status == 1) {
                modelCardService.approve(id, auditorId);
                result.put("code", 200);
                result.put("message", "审核通过");
            } else if (status == 2) {
                String rejectReason = (String) params.get("rejectReason");
                if (rejectReason == null || rejectReason.isEmpty()) {
                    result.put("code", 400);
                    result.put("message", "拒绝原因不能为空");
                    return result;
                }
                modelCardService.reject(id, rejectReason, auditorId);
                result.put("code", 200);
                result.put("message", "审核拒绝");
            } else {
                result.put("code", 400);
                result.put("message", "无效的审核状态");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "审核失败：" + e.getMessage());
        }
        return result;
    }

    // 获取待审核模特卡列表
    @GetMapping("/card/pending")
    public Map<String, Object> getPendingModelCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ModelCard> list = modelCardService.findPending(page, pageSize);
            int total = modelCardService.countPending();
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", list);
            data.put("total", total);
            data.put("page", page);
            data.put("pageSize", pageSize);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}