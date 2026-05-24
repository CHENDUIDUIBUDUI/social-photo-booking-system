package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Order;
import com.socialshoot.admin.mapper.OrderMapper;
import com.socialshoot.admin.service.OrderService;
import com.socialshoot.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class ApiOrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @GetMapping("/list")
    public Map<String, Object> getOrderList(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "user") String type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            List<Order> orderList;
            int total;
            
            if ("photographer".equals(type)) {
                orderList = orderService.findByPhotographerId(userId, page, pageSize);
                total = orderService.countByPhotographerId(userId);
            } else {
                orderList = orderService.findByUserId(userId, page, pageSize);
                total = orderService.countByUserId(userId);
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", orderList);
            data.put("total", total);
            data.put("page", page);
            data.put("pageSize", pageSize);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/detail")
    public Map<String, Object> getOrderDetail(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order order = orderService.findById(id);
            
            if (order == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("order", order);
            data.put("user", userService.getById(order.getUserId()));
            data.put("photographer", userService.getById(order.getPhotographerId()));
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/cancel")
    public Map<String, Object> cancelOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            Long orderId = Long.parseLong(params.get("id").toString());
            Order order = orderService.findById(orderId);
            
            if (order == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }
            
            if (!order.getUserId().equals(userId)) {
                result.put("code", 403);
                result.put("message", "无权操作");
                return result;
            }
            
            int rows = orderService.cancelOrder(orderId);
            if (rows > 0) {
                result.put("code", 200);
                result.put("message", "取消成功");
            } else {
                result.put("code", 500);
                result.put("message", "取消失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "取消失败：" + e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/complete")
    public Map<String, Object> completeOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            Long orderId = Long.parseLong(params.get("id").toString());
            Order order = orderService.findById(orderId);
            
            if (order == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }
            
            if (!order.getUserId().equals(userId) && !order.getPhotographerId().equals(userId)) {
                result.put("code", 403);
                result.put("message", "无权操作");
                return result;
            }
            
            orderMapper.updateStatus(orderId, 2);
            
            result.put("code", 200);
            result.put("message", "完成成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "完成失败：" + e.getMessage());
        }
        return result;
    }
    
    // 管理员获取所有订单列表
    @GetMapping("/admin/list")
    public Map<String, Object> getAdminOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long photographerId,
            @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 计算偏移量
            int offset = (pageNum - 1) * pageSize;
            
            // 构建查询条件
            StringBuilder sql = new StringBuilder("SELECT * FROM `order` WHERE 1=1");
            if (orderNo != null && !orderNo.isEmpty()) {
                sql.append(" AND order_no LIKE '%").append(orderNo).append("%'");
            }
            if (customerId != null) {
                sql.append(" AND user_id = " + customerId);
            }
            if (photographerId != null) {
                sql.append(" AND photographer_id = " + photographerId);
            }
            if (status != null) {
                sql.append(" AND status = " + status);
            }
            sql.append(" ORDER BY create_time DESC LIMIT " + offset + ", " + pageSize);
            
            // 执行查询
            List<Order> orderList = orderMapper.selectOrdersByCondition(sql.toString());
            
            // 计算总数
            StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM `order` WHERE 1=1");
            if (orderNo != null && !orderNo.isEmpty()) {
                countSql.append(" AND order_no LIKE '%").append(orderNo).append("%'");
            }
            if (customerId != null) {
                countSql.append(" AND user_id = " + customerId);
            }
            if (photographerId != null) {
                countSql.append(" AND photographer_id = " + photographerId);
            }
            if (status != null) {
                countSql.append(" AND status = " + status);
            }
            
            int total = orderMapper.countOrdersByCondition(countSql.toString());
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", orderList);
            data.put("total", total);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 管理员获取订单详情
    @GetMapping("/admin/detail/{id}")
    public Map<String, Object> getAdminOrderDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order order = orderService.findById(id);
            
            if (order == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("order", order);
            data.put("user", userService.getById(order.getUserId()));
            data.put("photographer", userService.getById(order.getPhotographerId()));
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 管理员修改订单
    @PostMapping("/admin/update")
    public Map<String, Object> updateOrder(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查订单是否存在
            Order existingOrder = orderService.findById(order.getId());
            if (existingOrder == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }
            
            // 更新订单
            int rows = orderMapper.updateOrder(order);
            if (rows > 0) {
                result.put("code", 200);
                result.put("message", "修改成功");
            } else {
                result.put("code", 500);
                result.put("message", "修改失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "修改失败：" + e.getMessage());
        }
        return result;
    }
    
    // 管理员删除订单
    @PostMapping("/admin/delete")
    public Map<String, Object> deleteOrder(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long orderId = Long.parseLong(params.get("id").toString());
            
            // 检查订单是否存在
            Order existingOrder = orderService.findById(orderId);
            if (existingOrder == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }
            
            // 删除订单
            int rows = orderMapper.deleteOrder(orderId);
            if (rows > 0) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 500);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    // 提交评价
    @PostMapping("/review")
    public Map<String, Object> submitReview(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            Long orderId = Long.parseLong(params.get("orderId").toString());
            Integer rating = Integer.valueOf(params.get("rating").toString());
            String comment = (String) params.get("comment");
            String type = (String) params.get("type"); // user 或 photographer
            
            if (orderId == null || rating == null || type == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            Order order = orderService.findById(orderId);
            if (order == null) {
                result.put("code", 404);
                result.put("message", "订单不存在");
                return result;
            }
            
            // 检查用户是否有权限评价
            if ("user".equals(type) && !order.getUserId().equals(userId)) {
                result.put("code", 403);
                result.put("message", "无权操作");
                return result;
            }
            if ("photographer".equals(type) && !order.getPhotographerId().equals(userId)) {
                result.put("code", 403);
                result.put("message", "无权操作");
                return result;
            }
            
            // 更新评价信息
            if ("user".equals(type)) {
                orderMapper.updateUserReview(orderId, rating, comment);
            } else if ("photographer".equals(type)) {
                orderMapper.updatePhotographerReview(orderId, rating, comment);
            }
            
            result.put("code", 200);
            result.put("message", "评价成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "评价失败：" + e.getMessage());
        }
        return result;
    }

    // 获取用户评价列表
    @GetMapping("/reviews/user/{userId}")
    public Map<String, Object> getUserReviews(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Order> reviews = orderService.findUserReviews(userId);
            result.put("code", 200);
            result.put("data", reviews);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取摄影师评价列表
    @GetMapping("/reviews/photographer/{photographerId}")
    public Map<String, Object> getPhotographerReviews(@PathVariable Long photographerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Order> reviews = orderService.findPhotographerReviews(photographerId);
            result.put("code", 200);
            result.put("data", reviews);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 根据userId获取用户订单列表
    @GetMapping("/user")
    public Map<String, Object> getUserOrders(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Order> orderList = orderService.findByUserId(userId, 1, 100);
            int total = orderService.countByUserId(userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", orderList);
            data.put("total", total);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    private Long getUserIdFromToken(String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            String[] parts = tokenValue.split("\\.");
            if (parts.length == 3) {
                String payload = parts[1];
                String decoded = new String(java.util.Base64.getUrlDecoder().decode(payload));
                return Long.parseLong(decoded.substring(decoded.indexOf("\"sub\":\"") + 7, decoded.indexOf("\",\"")));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}