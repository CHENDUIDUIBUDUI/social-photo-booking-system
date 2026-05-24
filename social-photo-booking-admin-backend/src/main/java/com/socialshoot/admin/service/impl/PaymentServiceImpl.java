package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Payment;
import com.socialshoot.admin.mapper.PaymentMapper;
import com.socialshoot.admin.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public void save(Payment payment) {
        paymentMapper.insert(payment);
    }

    @Override
    public Payment findByOrderNo(String orderNo) {
        return paymentMapper.findByOrderNo(orderNo);
    }

    @Override
    public Payment findByTransactionId(String transactionId) {
        return paymentMapper.findByTransactionId(transactionId);
    }

    @Override
    public Map<String, Object> createWechatPay(String orderNo, Double amount, String openId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 模拟微信支付参数生成
            // 实际项目中，这里需要调用微信支付API生成支付参数
            String prepayId = "wx" + System.currentTimeMillis();
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            String nonceStr = UUID.randomUUID().toString().replace("-", "");
            String sign = "mock_sign";
            
            // 保存支付记录
            Payment payment = new Payment();
            payment.setOrderNo(orderNo);
            payment.setAmount(amount);
            payment.setStatus(0); // 待支付
            payment.setPayType("wechat");
            payment.setCreateTime(LocalDateTime.now());
            payment.setUpdateTime(LocalDateTime.now());
            save(payment);
            
            Map<String, Object> payParams = new HashMap<>();
            payParams.put("prepayId", prepayId);
            payParams.put("timeStamp", timeStamp);
            payParams.put("nonceStr", nonceStr);
            payParams.put("sign", sign);
            payParams.put("orderNo", orderNo);
            
            result.put("code", 200);
            result.put("message", "获取支付参数成功");
            result.put("data", payParams);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取支付参数失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public void handleWechatPayCallback(Map<String, String> params) {
        try {
            // 模拟处理微信支付回调
            // 实际项目中，这里需要验证回调参数的真实性
            String orderNo = params.get("out_trade_no");
            String transactionId = params.get("transaction_id");
            String payTime = params.get("time_end");
            
            // 更新支付状态
            Payment payment = findByOrderNo(orderNo);
            if (payment != null) {
                payment.setTransactionId(transactionId);
                payment.setStatus(1); // 已支付
                payment.setPayTime(payTime);
                payment.setUpdateTime(LocalDateTime.now());
                paymentMapper.updateByOrderNo(payment);
                
                // 这里可以添加订单状态更新的逻辑
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}