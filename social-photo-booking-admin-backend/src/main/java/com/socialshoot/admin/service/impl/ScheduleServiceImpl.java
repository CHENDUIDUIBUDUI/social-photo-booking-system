package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Schedule;
import com.socialshoot.admin.mapper.ScheduleMapper;
import com.socialshoot.admin.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public Schedule create(Schedule schedule) {
        scheduleMapper.insert(schedule);
        return schedule;
    }

    @Override
    public Schedule findById(Long id) {
        return scheduleMapper.findById(id);
    }

    @Override
    public List<Schedule> findByPhotographerId(Long photographerId) {
        return scheduleMapper.findByPhotographerId(photographerId);
    }

    @Override
    public List<Schedule> findByOrderId(Long orderId) {
        return scheduleMapper.findByOrderId(orderId);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        return scheduleMapper.updateStatus(id, status);
    }

    @Override
    public int updateOrderId(Long id, Long orderId) {
        return scheduleMapper.updateOrderId(id, orderId);
    }

    @Override
    public int delete(Long id) {
        return scheduleMapper.delete(id);
    }
}