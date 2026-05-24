package com.photo.booking.controller;

import com.photo.booking.entity.Report;
import com.photo.booking.service.ReportService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @PostMapping("/create")
    public Map<String, Object> createReport(@RequestBody Report report) {
        Map<String, Object> result = new HashMap<>();
        try {
            Report created = reportService.createReport(report);
            result.put("code", 200);
            result.put("message", "举报成功");
            result.put("data", created);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "举报失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> getReportInfo(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Report report = reportService.getReportById(id);
            if (report != null) {
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", report);
            } else {
                result.put("code", 404);
                result.put("message", "举报不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/user")
    public Map<String, Object> getReportsByReporterId(@RequestParam Long reporterId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Report> reports = reportService.getReportsByReporterId(reporterId);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", reports);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/target")
    public Map<String, Object> getReportsByTarget(@RequestParam Long targetId, @RequestParam Integer type) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Report> reports = reportService.getReportsByTarget(targetId, type);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", reports);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/status")
    public Map<String, Object> getReportsByStatus(@RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Report> reports = reportService.getReportsByStatus(status);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", reports);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> getAllReports() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Report> reports = reportService.getAllReports();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", reports);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/status")
    public Map<String, Object> updateReportStatus(@RequestParam Long id, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean updated = reportService.updateReportStatus(id, status);
            if (updated) {
                result.put("code", 200);
                result.put("message", "状态更新成功");
            } else {
                result.put("code", 404);
                result.put("message", "举报不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }
}