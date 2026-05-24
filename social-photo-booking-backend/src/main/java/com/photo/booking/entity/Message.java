package com.photo.booking.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Message {
    private Long id;
    private Long sendUserId;
    private Long receiveUserId;
    private String content;
    private Integer type; // 0:私信, 1:订单通知, 2:系统通知
    private Integer status; // 0:未读, 1:已读
    private Long relatedId; // 关联ID（如订单ID）
    private Date createTime;
}
