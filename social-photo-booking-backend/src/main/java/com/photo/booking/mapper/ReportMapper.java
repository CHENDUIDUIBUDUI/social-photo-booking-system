package com.photo.booking.mapper;

import com.photo.booking.entity.Report;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {
    int insert(Report report);
    Report selectById(Long id);
    List<Report> selectByReporterId(Long reporterId);
    List<Report> selectByTarget(Map<String, Object> params);
    List<Report> selectByStatus(Integer status);
    List<Report> selectAll();
    int updateStatus(Map<String, Object> params);
}