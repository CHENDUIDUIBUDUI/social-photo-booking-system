package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private Long userId;
    private Long contentId;
    private String content;
    private Long parentId; // 父评论ID，用于回复功能
    private Integer likes;
    private LocalDateTime createTime;
}