package com.photo.booking.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Photographer {
    private Long id;
    private Long userId;
    private String name; // 摄影师昵称
    private String avatar; // 摄影师头像
    private Integer certified; // 认证状态: 0-未认证, 1-已认证
    private Date certifiedTime;
    private Integer orders; // 接单量
    private BigDecimal rating; // 好评率
    private BigDecimal price; // 起拍价格
    private String region; // 所在区域
    private String styles; // 擅长风格，逗号分隔
    private String coverImage; // 封面图片
    private String bio; // 个人简介
    private String works; // 作品集，JSON数组
    private Integer status; // 状态: 0-禁用, 1-正常
    private Date createTime;
    private Date updateTime;
}