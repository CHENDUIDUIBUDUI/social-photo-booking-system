package com.photo.booking.dto;

import lombok.Data;

@Data
public class OrderCreateDTO {
    private Long userId;
    private Long photographerId;
    private Long contentId;
    private Double totalAmount;
    private Double deposit; // 定金金额
    private Double paidDeposit; // 已付定金
    private Double balance; // 已付尾款
    private String location;
    private String shootTime; // 字符串格式：yyyy-MM-dd HH:mm:ss
    private String notes;
    private String contactName;
    private String contactPhone;
    private Integer status;
}
