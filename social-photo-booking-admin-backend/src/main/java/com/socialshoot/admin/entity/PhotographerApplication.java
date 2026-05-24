package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PhotographerApplication {
    private Long id;
    private Long userId;
    private String realName;
    private String idNumber;
    private String phone;
    private String styles; // JSON字符串存储擅长风格
    private String portfolio; // JSON字符串存储样片
    private Integer status; // 0: 待审核, 1: 审核通过, 2: 审核拒绝
    private String rejectReason;
    private Long auditorId; // 审核人ID
    private LocalDateTime auditTime; // 审核时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}