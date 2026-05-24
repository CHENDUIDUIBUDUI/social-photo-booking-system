package com.socialshoot.admin.entity;

import lombok.Data;

@Data
public class ReportHandleRequest {
    private Long id;
    private Integer status;
    private String handleResult;
    private Long handlerId;
}
