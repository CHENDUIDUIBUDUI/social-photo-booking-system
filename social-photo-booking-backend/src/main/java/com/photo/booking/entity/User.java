package com.photo.booking.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Long id;
    private String openid;
    private String nickname;
    private String avatar;
    private Integer gender;
    private String phone;
    private String email;
    private String password;
    private Integer role; // 0:普通用户, 1:摄影师, 2:模特
    private Integer status; // 0:禁用, 1:启用
    private Integer creditScore; // 信用分
    private Date createTime;
    private Date updateTime;
}
