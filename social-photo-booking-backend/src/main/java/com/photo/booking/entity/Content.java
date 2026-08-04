package com.photo.booking.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class Content {
    private Long id;
    private Long userId;
    private String title;
    private String name; // 非数据库字段，用于返回作品名称（值=title，供前端使用）
    private String description;
    private String coverImage;
    private String tags;
    private Double price;
    private String location;
    private Integer type; // 0:作品, 1:需求
    private Integer status; // 0:待审核, 1:审核通过, 2:审核拒绝
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Date createTime;
    private Date updateTime;
    
    // 非数据库字段，用于存储标签列表
    private List<String> tagList;
}
