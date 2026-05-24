package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.ModelCard;
import com.socialshoot.admin.service.ModelCardService;
import com.socialshoot.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/model")
public class ApiModelApplicationController {

    @Autowired
    private ModelCardService modelCardService;

    @Autowired
    private UserService userService;

    // 提交模特入驻申请
    @PostMapping("/application/submit")
    public Map<String, Object> submitApplication(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            
            // 检查是否已经提交过申请
            ModelCard existingModelCard = modelCardService.findByUserId(userId);
            if (existingModelCard != null) {
                result.put("code", 400);
                result.put("message", "您已经提交过申请，请等待审核");
                return result;
            }
            
            ModelCard modelCard = new ModelCard();
            modelCard.setUserId(userId);
            modelCard.setName((String) params.get("name"));
            modelCard.setHeight(Integer.valueOf(params.get("height").toString()));
            modelCard.setWeight(Integer.valueOf(params.get("weight").toString()));
            modelCard.setStyles((String) params.get("styles"));
            modelCard.setIsPaid(Integer.valueOf(params.get("isPaid").toString()));
            modelCard.setAvailability((String) params.get("availability"));
            modelCard.setPortfolio((String) params.get("portfolio"));
            modelCard.setIntroduction((String) params.get("introduction"));
            modelCard.setStatus(0); // 待审核
            modelCard.setCreateTime(LocalDateTime.now());
            modelCard.setUpdateTime(LocalDateTime.now());

            modelCardService.save(modelCard);

            result.put("code", 200);
            result.put("message", "申请提交成功，请等待审核");
            result.put("data", modelCard);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "提交失败：" + e.getMessage());
        }
        return result;
    }

    // 获取申请状态
    @GetMapping("/application/status")
    public Map<String, Object> getApplicationStatus(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            ModelCard modelCard = modelCardService.findByUserId(userId);
            if (modelCard != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", modelCard);
            } else {
                result.put("code", 404);
                result.put("message", "未提交申请");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取待审核申请列表
    @GetMapping("/application/pending")
    public Map<String, Object> getPendingApplications(
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

    // 审核申请
    @PostMapping("/application/audit")
    public Map<String, Object> auditApplication(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long id = Long.valueOf(params.get("id").toString());
            Integer status = Integer.valueOf(params.get("status").toString());
            String rejectReason = (String) params.get("rejectReason");
            Long auditorId = Long.valueOf(params.get("auditorId").toString());
            
            if (status == 1) {
                ModelCard modelCard = modelCardService.findById(id);
                if (modelCard != null) {
                    userService.updateRole(modelCard.getUserId(), 2);
                    modelCardService.approve(id, auditorId);
                    result.put("code", 200);
                    result.put("message", "审核通过");
                } else {
                    result.put("code", 404);
                    result.put("message", "申请不存在");
                }
            } else if (status == 2) {
                // 审核拒绝
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

    // 获取模特数据看板
    @GetMapping("/dashboard")
    public Map<String, Object> getModelDashboard(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 这里可以实现模特的数据统计，如合作次数、评价等
            // 由于没有具体的实现，暂时返回模拟数据
            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("totalCooperations", 15);
            dashboardData.put("completedCooperations", 12);
            dashboardData.put("averageRating", 4.9);
            dashboardData.put("profileViews", 203);
            dashboardData.put("collections", 45);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", dashboardData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}