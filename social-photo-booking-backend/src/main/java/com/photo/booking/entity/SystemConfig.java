package com.photo.booking.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SystemConfig {
    private Long id;
    private String key;
    private String value;
    private String description;
    private Date createTime;
    private Date updateTime;
}
