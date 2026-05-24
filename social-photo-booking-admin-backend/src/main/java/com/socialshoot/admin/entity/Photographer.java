package com.socialshoot.admin.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Photographer {
    private Long id;
    private Long userId;
    private String name;
    private String avatar;
    private Integer certified;
    private Date certifiedTime;
    private Integer orders;
    private BigDecimal rating;
    private BigDecimal price;
    private String region;
    private String styles;
    private String coverImage;
    private String bio;
    private String works;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}