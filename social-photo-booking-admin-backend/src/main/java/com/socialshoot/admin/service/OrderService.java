package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> findByUserId(Long userId, int page, int pageSize);
    List<Order> findByPhotographerId(Long photographerId, int page, int pageSize);
    Order findById(Long id);
    int countByUserId(Long userId);
    int countByPhotographerId(Long photographerId);
    List<Order> findUserReviews(Long userId);

    List<Order> findPhotographerReviews(Long photographerId);
    
    int cancelOrder(Long orderId);
}