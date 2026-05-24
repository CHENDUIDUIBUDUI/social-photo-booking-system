package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ModelCard {
    private Long id;
    private Long userId;
    private String name;
    private Integer height;
    private Integer weight;
    private String styles; // JSON字符串存储风格数组
    private Integer isPaid; // 0: 无偿, 1: 有偿
    private String availability; // 可约档期
    private String portfolio; // JSON字符串存储作品相册
    private String introduction;
    private Integer status; // 0: 待审核, 1: 审核通过, 2: 审核拒绝
    private String rejectReason; // 拒绝原因
    private Long auditorId; // 审核人ID
    private LocalDateTime auditTime; // 审核时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}