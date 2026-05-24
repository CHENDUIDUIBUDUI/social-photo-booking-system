package com.photo.booking.controller;

import com.photo.booking.dto.OrderCreateDTO;
import com.photo.booking.entity.Order;
import com.photo.booking.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody OrderCreateDTO orderDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order order = new Order();
            order.setUserId(orderDTO.getUserId());
            order.setPhotographerId(orderDTO.getPhotographerId());
            order.setContentId(orderDTO.getContentId());
            order.setTotalAmount(orderDTO.getTotalAmount());
            // 使用前端传递的定金和尾款值
            order.setDeposit(orderDTO.getDeposit()); // 定金金额
            order.setPaidDeposit(orderDTO.getPaidDeposit()); // 已付定金
            order.setBalance(orderDTO.getBalance()); // 已付尾款
            order.setLocation(orderDTO.getLocation());
            order.setNotes(orderDTO.getNotes());
            order.setContactName(orderDTO.getContactName());
            order.setContactPhone(orderDTO.getContactPhone());
            order.setStatus(orderDTO.getStatus());
            
            // 转换shootTime字符串为Date
            if (orderDTO.getShootTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                order.setShootTime(sdf.parse(orderDTO.getShootTime()));
            }
            
            Order created = orderService.createOrder(order);
            result.put("code", 200);
            result.put("message", "预约成功");
            result.put("data", created);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "预约失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> getOrderInfo(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order order = orderService.getOrderById(id);
            if (order != null) {
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", order);
            } else {
                result.put("code", 404);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/orderNo")
    public Map<String, Object> getOrderByOrderNo(@RequestParam String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order order = orderService.getOrderByOrderNo(orderNo);
            if (order != null) {
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", order);
            } else {
                result.put("code", 404);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> updateOrder(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order updated = orderService.updateOrder(order);
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", updated);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/status")
    public Map<String, Object> updateOrderStatus(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Integer status = Integer.valueOf(request.get("status").toString());
            orderService.updateOrderStatus(id, status);
            result.put("code", 200);
            result.put("message", "状态更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/rating")
    public Map<String, Object> updateOrderRating(@RequestParam Long id, @RequestParam Integer userRating, @RequestParam String userComment, @RequestParam Integer photographerRating, @RequestParam String photographerComment) {
        Map<String, Object> result = new HashMap<>();
        try {
            orderService.updateOrderRating(id, userRating, userComment, photographerRating, photographerComment);
            result.put("code", 200);
            result.put("message", "评价成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "评价失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/user")
    public Map<String, Object> getOrderByUserId(@RequestParam Long userId, @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Order> orders = orderService.getOrderByUserId(userId, status);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", orders);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/photographer")
    public Map<String, Object> getOrderByPhotographerId(@RequestParam Long photographerId, @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Order> orders = orderService.getOrderByPhotographerId(photographerId, status);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", orders);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> getOrderList(@RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Order> orders = orderService.getOrderList(status);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", orders);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }
}
