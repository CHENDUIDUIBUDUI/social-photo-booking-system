package com.socialshoot.admin.controller;

import com.socialshoot.admin.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class ApiPaymentController {

    @Autowired
    private PaymentService paymentService;

    // 创建微信支付
    @PostMapping("/wechat/create")
    public Map<String, Object> createWechatPay(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderNo = (String) params.get("orderNo");
            Double amount = Double.valueOf(params.get("amount").toString());
            String openId = (String) params.get("openId");
            
            if (orderNo == null || amount == null || openId == null) {
                result.put("code", 400);
                result.put("message", "参数不能为空");
                return result;
            }
            
            Map<String, Object> payResult = paymentService.createWechatPay(orderNo, amount, openId);
            return payResult;
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "创建支付失败：" + e.getMessage());
            return result;
        }
    }

    // 处理微信支付回调
    @PostMapping("/wechat/callback")
    public Map<String, Object> handleWechatCallback(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            paymentService.handleWechatPayCallback(params);
            
            // 返回微信支付回调成功响应
            result.put("code", "SUCCESS");
            result.put("message", "成功");
        } catch (Exception e) {
            result.put("code", "FAIL");
            result.put("message", "失败：" + e.getMessage());
        }
        return result;
    }

    // 查询支付状态
    @GetMapping("/status")
    public Map<String, Object> getPaymentStatus(@RequestParam String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            var payment = paymentService.findByOrderNo(orderNo);
            if (payment != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", payment);
            } else {
                result.put("code", 404);
                result.put("message", "支付记录不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}