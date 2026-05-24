package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Like {
    private Long id;
    private Long userId;
    private Long contentId;
    private Integer type; // 1: 点赞, 2: 收藏
    private LocalDateTime createTime;
}