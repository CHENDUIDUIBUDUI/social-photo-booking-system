package com.photo.booking.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Package {
    private Long id;
    private Long photographerId;
    private String name;
    private Double price;
    private String description;
    private String features;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}