package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Report;
import com.socialshoot.admin.entity.ReportDetail;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<ReportDetail> getReportList(Integer status, Integer type);
    ReportDetail getReportDetail(Long id);
    int handleReport(Long id, Integer status, String handleResult, Long handlerId);
    int createReport(Report report);
    Map<String, Object> getReportStatistics();
}
