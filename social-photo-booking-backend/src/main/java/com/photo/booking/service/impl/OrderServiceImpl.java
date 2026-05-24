package com.photo.booking.service.impl;

import com.photo.booking.entity.Order;
import com.photo.booking.mapper.OrderMapper;
import com.photo.booking.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public Order createOrder(Order order) {
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        orderMapper.insert(order);
        return order;
    }
    
    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        return "YP" + timestamp;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public Order updateOrder(Order order) {
        orderMapper.update(order);
        return order;
    }

    @Override
    public void updateOrderStatus(Long id, Integer status) {
        orderMapper.updateStatus(id, status);
    }

    @Override
    public void updateOrderRating(Long id, Integer userRating, String userComment, Integer photographerRating, String photographerComment) {
        orderMapper.updateRating(id, userRating, userComment, photographerRating, photographerComment);
    }

    @Override
    public List<Order> getOrderByUserId(Long userId, Integer status) {
        return orderMapper.selectByUserId(userId, status);
    }

    @Override
    public List<Order> getOrderByPhotographerId(Long photographerId, Integer status) {
        return orderMapper.selectByPhotographerId(photographerId, status);
    }

    @Override
    public List<Order> getOrderList(Integer status) {
        return orderMapper.selectList(status);
    }
}
