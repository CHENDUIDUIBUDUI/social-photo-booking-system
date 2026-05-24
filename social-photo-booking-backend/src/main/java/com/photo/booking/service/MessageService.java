package com.photo.booking.service;

import com.photo.booking.entity.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(Message message);
    void updateMessageStatus(Long id, Integer status);
    void updateMessageStatusByUserId(Long userId, Integer status);
    List<Message> getMessageByUserId(Long userId, Integer type);
    List<Message> getChatMessages(Long userId1, Long userId2);
    int countUnreadMessage(Long userId);
}
