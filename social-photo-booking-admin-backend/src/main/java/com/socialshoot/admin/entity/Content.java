package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Content {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String coverImage; // 封面图片URL
    private String location;
    private Long locationId; // 关联location表的id
    private Integer priceType; // 价格类型
    private Double price;
    private Double deposit; // 定金
    private Integer type;
    private Integer status;
    private String tags; // JSON array of tags
    private String auditReason; // 审核拒绝理由
    private Integer viewCount; // 浏览量
    private Integer likeCount; // 点赞数
    private Integer commentCount; // 评论数
    private Integer likes; // 兼容字段
    private Integer comments; // 兼容字段
    private Integer views; // 兼容字段
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}