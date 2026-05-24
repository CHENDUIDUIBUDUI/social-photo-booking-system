package com.photo.booking.entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserProfile {
    private Long id;
    private Long userId;
    private String realName;
    private String idCard;
    private String city;
    private String introduction;
    private String tags;
    private String portfolio;
    private Integer serviceType; // 服务类型
    private Double price; // 价格
    private Date createTime;
    private Date updateTime;
}
