package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Report;
import com.socialshoot.admin.entity.ReportDetail;
import com.socialshoot.admin.entity.ReportHandleRequest;
import com.socialshoot.admin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/list")
    public Map<String, Object> getReportList(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer type) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ReportDetail> reportList = reportService.getReportList(status, type);
            result.put("code", 200);
            result.put("data", reportList);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/detail")
    public Map<String, Object> getReportDetail(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            ReportDetail reportDetail = reportService.getReportDetail(id);
            if (reportDetail != null) {
                result.put("code", 200);
                result.put("data", reportDetail);
            } else {
                result.put("code", 404);
                result.put("message", "举报记录不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/handle")
    public Map<String, Object> handleReport(@RequestBody ReportHandleRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            int success = reportService.handleReport(request.getId(), request.getStatus(), request.getHandleResult(), request.getHandlerId());
            if (success > 0) {
                result.put("code", 200);
                result.put("message", "处理成功");
            } else {
                result.put("code", 400);
                result.put("message", "处理失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create")
    public Map<String, Object> createReport(@RequestBody Report report) {
        Map<String, Object> result = new HashMap<>();
        try {
            int success = reportService.createReport(report);
            if (success > 0) {
                result.put("code", 200);
                result.put("message", "创建成功");
            } else {
                result.put("code", 400);
                result.put("message", "创建失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/statistics")
    public Map<String, Object> getReportStatistics() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> statistics = reportService.getReportStatistics();
            result.put("code", 200);
            result.put("data", statistics);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}
