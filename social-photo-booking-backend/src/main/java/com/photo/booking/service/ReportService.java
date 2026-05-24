package com.photo.booking.service;

import com.photo.booking.entity.Report;
import java.util.List;

public interface ReportService {
    Report createReport(Report report);
    Report getReportById(Long id);
    List<Report> getReportsByReporterId(Long reporterId);
    List<Report> getReportsByTarget(Long targetId, Integer type);
    List<Report> getReportsByStatus(Integer status);
    List<Report> getAllReports();
    boolean updateReportStatus(Long id, Integer status);
}