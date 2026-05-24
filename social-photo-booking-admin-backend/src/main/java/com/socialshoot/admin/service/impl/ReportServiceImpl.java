package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.*;
import com.socialshoot.admin.mapper.*;
import com.socialshoot.admin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public List<ReportDetail> getReportList(Integer status, Integer type) {
        List<Report> reports = reportMapper.selectByCondition(status, type);
        List<ReportDetail> reportDetails = new ArrayList<>();

        for (Report report : reports) {
            ReportDetail detail = convertToDetail(report);
            reportDetails.add(detail);
        }

        return reportDetails;
    }

    @Override
    public ReportDetail getReportDetail(Long id) {
        Report report = reportMapper.selectById(id);
        if (report == null) {
            return null;
        }
        return convertToDetail(report);
    }

    @Override
    public int handleReport(Long id, Integer status, String handleResult, Long handlerId) {
        return reportMapper.updateHandle(id, status, handleResult, handlerId);
    }

    @Override
    public int createReport(Report report) {
        return reportMapper.insert(report);
    }

    @Override
    public Map<String, Object> getReportStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        int totalReports = reportMapper.countByStatus(null);
        int pendingReports = reportMapper.countByStatus(0);
        int handledReports = reportMapper.countByStatus(1);
        int rejectedReports = reportMapper.countByStatus(2);
        
        int contentReports = reportMapper.countByType(1);
        int commentReports = reportMapper.countByType(2);
        int userReports = reportMapper.countByType(3);

        statistics.put("totalReports", totalReports);
        statistics.put("pendingReports", pendingReports);
        statistics.put("handledReports", handledReports);
        statistics.put("rejectedReports", rejectedReports);
        statistics.put("contentReports", contentReports);
        statistics.put("commentReports", commentReports);
        statistics.put("userReports", userReports);

        return statistics;
    }

    private ReportDetail convertToDetail(Report report) {
        ReportDetail detail = new ReportDetail();
        detail.setId(report.getId());
        detail.setReporterId(report.getReporterId());
        detail.setType(report.getType());
        detail.setTargetId(report.getTargetId());
        detail.setReason(report.getReason());
        detail.setImages(report.getImages());
        detail.setStatus(report.getStatus());
        detail.setHandleResult(report.getHandleResult());
        detail.setHandlerId(report.getHandlerId());
        detail.setHandleTime(report.getHandleTime());
        detail.setCreateTime(report.getCreateTime());

        String typeName = "";
        switch (report.getType()) {
            case 1:
                typeName = "内容";
                break;
            case 2:
                typeName = "评论";
                break;
            case 3:
                typeName = "用户";
                break;
        }
        detail.setTypeName(typeName);

        String statusName = "";
        switch (report.getStatus()) {
            case 0:
                statusName = "待处理";
                break;
            case 1:
                statusName = "已处理";
                break;
            case 2:
                statusName = "驳回";
                break;
        }
        detail.setStatusName(statusName);

        User reporter = userMapper.selectById(report.getReporterId());
        if (reporter != null) {
            detail.setReporterNickname(reporter.getNickname());
            detail.setReporterAvatar(reporter.getAvatar());
        }

        if (report.getType() == 1) {
            Content content = contentMapper.selectById(report.getTargetId());
            if (content != null) {
                detail.setTargetTitle(content.getTitle());
                detail.setTargetContent(content.getDescription());
                // 查询内容作者信息
                User targetUser = userMapper.selectById(content.getUserId());
                if (targetUser != null) {
                    detail.setTargetUserNickname(targetUser.getNickname());
                }
            }
        } else if (report.getType() == 2) {
            User targetUser = userMapper.selectById(report.getTargetId());
            if (targetUser != null) {
                detail.setTargetUserNickname(targetUser.getNickname());
            }
        } else if (report.getType() == 3) {
            User targetUser = userMapper.selectById(report.getTargetId());
            if (targetUser != null) {
                detail.setTargetUserNickname(targetUser.getNickname());
            }
        }

        if (report.getHandlerId() != null) {
            AdminUser handler = adminUserMapper.selectById(report.getHandlerId());
            if (handler != null) {
                detail.setHandlerName(handler.getRealName() != null ? handler.getRealName() : handler.getUsername());
            }
        }

        return detail;
    }
}
