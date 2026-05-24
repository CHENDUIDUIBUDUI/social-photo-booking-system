package com.photo.booking.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Like {
    private Long id;
    private Long userId;
    private Long contentId;
    private Date createTime;
}
