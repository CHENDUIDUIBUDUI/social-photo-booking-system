package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportDetail {
    private Long id;
    private Long reporterId;
    private String reporterNickname;
    private String reporterAvatar;
    private Integer type;
    private String typeName;
    private Long targetId;
    private String targetTitle;
    private String targetContent;
    private String targetUserNickname;
    private String reason;
    private String images;
    private Integer status;
    private String statusName;
    private String handleResult;
    private Long handlerId;
    private String handlerName;
    private LocalDateTime handleTime;
    private LocalDateTime createTime;
}
