package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Photographer;
import com.socialshoot.admin.entity.PhotographerApplication;
import com.socialshoot.admin.mapper.PhotographerMapper;
import com.socialshoot.admin.service.PhotographerApplicationService;
import com.socialshoot.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photographer")
public class ApiPhotographerApplicationController {

    @Autowired
    private PhotographerApplicationService photographerApplicationService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private PhotographerMapper photographerMapper;

    // 提交摄影师入驻申请
    @PostMapping("/application/submit")
    public Map<String, Object> submitApplication(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            
            // 检查是否已经提交过申请
            PhotographerApplication existingApplication = photographerApplicationService.findByUserId(userId);
            if (existingApplication != null) {
                result.put("code", 400);
                result.put("message", "您已经提交过申请，请等待审核");
                return result;
            }
            
            PhotographerApplication application = new PhotographerApplication();
            application.setUserId(userId);
            application.setRealName((String) params.get("realName"));
            application.setIdNumber((String) params.get("idNumber"));
            application.setPhone((String) params.get("phone"));
            application.setStyles((String) params.get("styles"));
            application.setPortfolio((String) params.get("portfolio"));
            application.setStatus(0); // 待审核
            application.setCreateTime(LocalDateTime.now());
            application.setUpdateTime(LocalDateTime.now());

            photographerApplicationService.save(application);

            result.put("code", 200);
            result.put("message", "申请提交成功，请等待审核");
            result.put("data", application);
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
            PhotographerApplication application = photographerApplicationService.findByUserId(userId);
            if (application != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", application);
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
            List<PhotographerApplication> list = photographerApplicationService.findPending(page, pageSize);
            int total = photographerApplicationService.countPending();
            
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
                PhotographerApplication application = photographerApplicationService.findById(id);
                if (application != null) {
                    userService.updateRole(application.getUserId(), 1);
                    photographerApplicationService.approve(id, auditorId);
                    
                    // 创建摄影师记录
                    Photographer photographer = new Photographer();
                    photographer.setUserId(application.getUserId());
                    photographer.setName(application.getRealName());
                    photographer.setStyles(application.getStyles());
                    photographer.setCertified(1);
                    photographer.setCertifiedTime(new Date());
                    photographer.setOrders(0);
                    photographer.setRating(new BigDecimal("0.0"));
                    photographer.setPrice(new BigDecimal("0.0"));
                    photographer.setStatus(1);
                    photographer.setCreateTime(new Date());
                    photographer.setUpdateTime(new Date());
                    photographerMapper.insert(photographer);
                    
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
                photographerApplicationService.reject(id, rejectReason, auditorId);
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

    // 获取摄影师数据看板
    @GetMapping("/dashboard")
    public Map<String, Object> getPhotographerDashboard(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 这里可以实现摄影师的数据统计，如接单量、收入、评价等
            // 由于没有具体的实现，暂时返回模拟数据
            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("totalOrders", 12);
            dashboardData.put("completedOrders", 8);
            dashboardData.put("totalIncome", 5000.0);
            dashboardData.put("averageRating", 4.8);
            dashboardData.put("profileViews", 156);
            
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