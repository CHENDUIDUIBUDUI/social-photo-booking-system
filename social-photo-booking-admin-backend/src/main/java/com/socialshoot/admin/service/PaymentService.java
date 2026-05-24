package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Payment;
import java.util.Map;

public interface PaymentService {
    void save(Payment payment);
    Payment findByOrderNo(String orderNo);
    Payment findByTransactionId(String transactionId);
    Map<String, Object> createWechatPay(String orderNo, Double amount, String openId);
    void handleWechatPayCallback(Map<String, String> params);
}