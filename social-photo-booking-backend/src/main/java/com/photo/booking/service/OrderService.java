package com.photo.booking.service;

import com.photo.booking.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrderById(Long id);
    Order getOrderByOrderNo(String orderNo);
    Order updateOrder(Order order);
    void updateOrderStatus(Long id, Integer status);
    void updateOrderRating(Long id, Integer userRating, String userComment, Integer photographerRating, String photographerComment);
    List<Order> getOrderByUserId(Long userId, Integer status);
    List<Order> getOrderByPhotographerId(Long photographerId, Integer status);
    List<Order> getOrderList(Integer status);
}
