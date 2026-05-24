package com.photo.booking.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Long id;
    private Long contentId;
    private Long userId;
    private String content;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    // 扩展字段，用于返回给前端
    private String nickname;
    private String avatar;
    private int likeCount;
    private boolean isLiked;
}
