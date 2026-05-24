package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Payment {
    private Long id;
    private String orderNo;
    private Long userId;
    private Double amount;
    private String transactionId;
    private Integer status; // 0: 待支付, 1: 已支付, 2: 支付失败
    private String payType; // wechat: 微信支付
    private String payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}