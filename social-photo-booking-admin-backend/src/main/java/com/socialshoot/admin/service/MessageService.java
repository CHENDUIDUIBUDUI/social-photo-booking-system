package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> findByUserId(Long userId, int page, int pageSize);
    int countByUserId(Long userId);
    List<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, int page, int pageSize);
    int countBySenderIdAndReceiverId(Long senderId, Long receiverId);
    int countUnreadByUserId(Long userId);
}