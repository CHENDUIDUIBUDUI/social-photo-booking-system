package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String openId;
    private String unionId;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer roleId;
    private Integer creditScore;
    private String specialty;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
