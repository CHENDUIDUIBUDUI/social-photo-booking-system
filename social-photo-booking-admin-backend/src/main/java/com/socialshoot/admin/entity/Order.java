package com.socialshoot.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String order_no;
    private Long user_id;
    private Long photographer_id;
    private Long content_id;
    private Double deposit_amount;
    private Double paid_balance;
    private Double total_amount;
    private String location;
    private String shoot_time;
    private String notes;
    private Long schedule_id;
    private Integer status;
    private Integer user_rating;
    private String user_comment;
    private Integer photographer_rating;
    private String photographer_comment;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
    
    // 为了兼容旧代码，添加 getUserId 和 getPhotographerId 方法
    public Long getUserId() {
        return user_id;
    }
    
    public Long getPhotographerId() {
        return photographer_id;
    }
    
    public Long getScheduleId() {
        return schedule_id;
    }
}