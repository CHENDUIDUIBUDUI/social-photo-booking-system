package com.photo.booking.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long photographerId;
    private Long contentId;
    private Double totalAmount;
    private Double deposit; // 定金金额
    private Double paidDeposit; // 已付定金
    private Double balance; // 已付尾款
    private String location;
    private Date shootTime;
    private String notes;
    private String contactName;
    private String contactPhone;
    private Integer status; // 0:待付款, 1:已支付定金, 2:进行中, 3:拍摄完成, 4:已完成, 5:已取消
    private Integer userRating;
    private String userComment;
    private Integer photographerRating;
    private String photographerComment;
    private Date createTime;
    private Date updateTime;
}
