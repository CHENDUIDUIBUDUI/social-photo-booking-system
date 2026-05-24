package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Collection {
    private Long id;
    private Long userId;
    private Long contentId;
    private LocalDateTime createTime;
}