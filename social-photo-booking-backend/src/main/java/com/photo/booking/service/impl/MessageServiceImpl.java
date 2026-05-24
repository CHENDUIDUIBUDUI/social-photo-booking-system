package com.photo.booking.service.impl;

import com.photo.booking.entity.Message;
import com.photo.booking.mapper.MessageMapper;
import com.photo.booking.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public Message sendMessage(Message message) {
        messageMapper.insert(message);
        return message;
    }

    @Override
    public void updateMessageStatus(Long id, Integer status) {
        messageMapper.updateStatus(id, status);
    }

    @Override
    public void updateMessageStatusByUserId(Long userId, Integer status) {
        messageMapper.updateStatusByReceiveUserId(userId, status);
    }

    @Override
    public List<Message> getMessageByUserId(Long userId, Integer type) {
        return messageMapper.selectByReceiveUserId(userId, type);
    }

    @Override
    public List<Message> getChatMessages(Long userId1, Long userId2) {
        return messageMapper.selectChatMessages(userId1, userId2);
    }

    @Override
    public int countUnreadMessage(Long userId) {
        return messageMapper.countUnreadByUserId(userId);
    }
}
