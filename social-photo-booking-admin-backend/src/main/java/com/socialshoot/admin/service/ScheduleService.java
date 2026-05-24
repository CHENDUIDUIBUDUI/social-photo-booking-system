package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule create(Schedule schedule);
    Schedule findById(Long id);
    List<Schedule> findByPhotographerId(Long photographerId);
    List<Schedule> findByOrderId(Long orderId);
    int updateStatus(Long id, Integer status);
    int updateOrderId(Long id, Long orderId);
    int delete(Long id);
}