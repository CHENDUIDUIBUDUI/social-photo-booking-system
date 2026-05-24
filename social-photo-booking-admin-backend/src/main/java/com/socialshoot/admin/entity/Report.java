package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Report {
    private Long id;
    private Long reporterId;
    private Integer type;
    private Long targetId;
    private String reason;
    private String images;
    private Integer status;
    private String handleResult;
    private Long handlerId;
    private LocalDateTime handleTime;
    private LocalDateTime createTime;
}
