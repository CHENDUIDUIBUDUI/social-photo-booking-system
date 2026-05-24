package com.socialshoot.admin.controller;

import com.socialshoot.admin.mapper.UserMapper;
import com.socialshoot.admin.mapper.OrderMapper;
import com.socialshoot.admin.mapper.PhotographerApplicationMapper;
import com.socialshoot.admin.mapper.ModelCardMapper;
import com.socialshoot.admin.mapper.ContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PhotographerApplicationMapper photographerApplicationMapper;

    @Autowired
    private ModelCardMapper modelCardMapper;

    @Autowired
    private ContentMapper contentMapper;

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> result = new HashMap<>();
        try {
            int userCount = userMapper.countAllUsers();
            int photographerCount = userMapper.countPhotographers();
            int modelCount = userMapper.countByRole(2);
            int normalUserCount = userMapper.countByRole(3);

            int orderCount = orderMapper.countAllOrders();
            Double totalAmountDouble = orderMapper.sumTotalAmount();
            double totalAmount = totalAmountDouble != null ? totalAmountDouble : 0.00;

            int pendingPhotographerCount = photographerApplicationMapper.countPending();
            int pendingModelCount = modelCardMapper.countPending();
            int pendingContentCount = contentMapper.countPending();
            int totalPendingCount = pendingPhotographerCount + pendingModelCount + pendingContentCount;

            Map<String, Object> stats = new HashMap<>();
            stats.put("userCount", userCount);
            stats.put("photographerCount", photographerCount);
            stats.put("modelCount", modelCount);
            stats.put("normalUserCount", normalUserCount);
            stats.put("orderCount", orderCount);
            stats.put("totalAmount", totalAmount);
            stats.put("pendingPhotographerCount", pendingPhotographerCount);
            stats.put("pendingModelCount", pendingModelCount);
            stats.put("pendingContentCount", pendingContentCount);
            stats.put("totalPendingCount", totalPendingCount);

            result.put("code", 200);
            result.put("data", stats);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/charts")
    public Map<String, Object> getChartsData() {
        Map<String, Object> result = new HashMap<>();
        try {
            int photographerCount = userMapper.countPhotographers();
            int modelCount = userMapper.countByRole(2);
            int normalUserCount = userMapper.countByRole(3);

            List<Map<String, Object>> userDistribution = new ArrayList<>();

            Map<String, Object> normalUser = new HashMap<>();
            normalUser.put("value", normalUserCount);
            normalUser.put("name", "普通用户");
            userDistribution.add(normalUser);

            Map<String, Object> photographer = new HashMap<>();
            photographer.put("value", photographerCount);
            photographer.put("name", "摄影师");
            userDistribution.add(photographer);

            Map<String, Object> model = new HashMap<>();
            model.put("value", modelCount);
            model.put("name", "模特");
            userDistribution.add(model);

            List<Map<String, Object>> orderTrendData = orderMapper.getOrderTrend();
            List<String> months = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();

            if (orderTrendData != null && !orderTrendData.isEmpty()) {
                for (Map<String, Object> data : orderTrendData) {
                    String monthStr = (String) data.get("month");
                    String[] parts = monthStr.split("-");
                    months.add(parts[1] + "月");
                    counts.add(Integer.parseInt(data.get("count").toString()));
                }
            }

            Map<String, Object> orderTrend = new HashMap<>();
            orderTrend.put("months", months);
            orderTrend.put("counts", counts);

            Map<String, Object> chartsData = new HashMap<>();
            chartsData.put("userDistribution", userDistribution);
            chartsData.put("orderTrend", orderTrend);

            result.put("code", 200);
            result.put("data", chartsData);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}
