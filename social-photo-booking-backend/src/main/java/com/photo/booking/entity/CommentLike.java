package com.photo.booking.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CommentLike {
    private Long id;
    private Long userId;
    private Long commentId;
    private Date createTime;
}
