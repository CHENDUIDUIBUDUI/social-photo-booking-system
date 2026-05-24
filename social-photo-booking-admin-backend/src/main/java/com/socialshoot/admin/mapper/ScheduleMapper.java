package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    int insert(Schedule schedule);
    Schedule findById(Long id);
    List<Schedule> findByPhotographerId(Long photographerId);
    List<Schedule> findByOrderId(Long orderId);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int updateOrderId(@Param("id") Long id, @Param("orderId") Long orderId);
    int delete(Long id);
}