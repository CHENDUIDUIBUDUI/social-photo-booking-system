package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminLog {
    private Long id;
    private Long adminId;
    private String module;
    private String operation;
    private String method;
    private String url;
    private String params;
    private String ip;
    private Integer duration;
    private Integer result;
    private String errorMsg;
    private LocalDateTime createTime;
}