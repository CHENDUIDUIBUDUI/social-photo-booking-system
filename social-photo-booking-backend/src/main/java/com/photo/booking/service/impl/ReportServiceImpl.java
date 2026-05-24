package com.photo.booking.service.impl;

import com.photo.booking.entity.Report;
import com.photo.booking.mapper.ReportMapper;
import com.photo.booking.service.ReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Override
    public Report createReport(Report report) {
        // 设置默认状态为0（待处理）
        if (report.getStatus() == null) {
            report.setStatus(0);
        }
        reportMapper.insert(report);
        return report;
    }

    @Override
    public Report getReportById(Long id) {
        return reportMapper.selectById(id);
    }

    @Override
    public List<Report> getReportsByReporterId(Long reporterId) {
        return reportMapper.selectByReporterId(reporterId);
    }

    @Override
    public List<Report> getReportsByTarget(Long targetId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("targetId", targetId);
        params.put("type", type);
        return reportMapper.selectByTarget(params);
    }

    @Override
    public List<Report> getReportsByStatus(Integer status) {
        return reportMapper.selectByStatus(status);
    }

    @Override
    public List<Report> getAllReports() {
        return reportMapper.selectAll();
    }

    @Override
    public boolean updateReportStatus(Long id, Integer status) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("status", status);
        return reportMapper.updateStatus(params) > 0;
    }
}