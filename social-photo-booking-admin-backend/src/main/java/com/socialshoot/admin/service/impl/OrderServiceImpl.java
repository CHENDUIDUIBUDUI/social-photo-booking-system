package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Order;
import com.socialshoot.admin.entity.Schedule;
import com.socialshoot.admin.mapper.OrderMapper;
import com.socialshoot.admin.service.OrderService;
import com.socialshoot.admin.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Override
    public List<Order> findByUserId(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Order> orders = orderMapper.findByUserId(userId, offset, pageSize);
        return orders;
    }
    
    @Override
    public List<Order> findByPhotographerId(Long photographerId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Order> orders = orderMapper.findByPhotographerId(photographerId, offset, pageSize);
        return orders;
    }
    
    @Override
    public Order findById(Long id) {
        Order order = orderMapper.findById(id);
        return order;
    }
    
    @Override
    public int countByUserId(Long userId) {
        return orderMapper.countByUserId(userId);
    }
    
    @Override
    public int countByPhotographerId(Long photographerId) {
        return orderMapper.countByPhotographerId(photographerId);
    }

    @Override
    public List<Order> findUserReviews(Long userId) {
        return orderMapper.findUserReviews(userId);
    }

    @Override
    public List<Order> findPhotographerReviews(Long photographerId) {
        return orderMapper.findPhotographerReviews(photographerId);
    }
    
    @Transactional
    @Override
    public int cancelOrder(Long orderId) {
        // 1. 查找订单
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            return 0;
        }
        
        // 2. 更新订单状态为已取消
        int orderResult = orderMapper.updateStatus(orderId, 5); // 5表示已取消
        
        // 3. 释放关联的档期
        if (order.getScheduleId() != null) {
            Schedule schedule = scheduleService.findById(order.getScheduleId());
            if (schedule != null) {
                scheduleService.updateStatus(schedule.getId(), 0); // 0表示可用
                scheduleService.updateOrderId(schedule.getId(), null);
            }
        }
        
        return orderResult;
    }
}