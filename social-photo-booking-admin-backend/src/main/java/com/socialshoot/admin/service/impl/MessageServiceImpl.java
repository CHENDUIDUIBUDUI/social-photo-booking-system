package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Message;
import com.socialshoot.admin.mapper.MessageMapper;
import com.socialshoot.admin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Override
    public List<Message> findByUserId(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return messageMapper.findByUserId(userId, offset, pageSize);
    }
    
    @Override
    public int countByUserId(Long userId) {
        return messageMapper.countByUserId(userId);
    }
    
    @Override
    public List<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return messageMapper.findBySenderIdAndReceiverId(senderId, receiverId, offset, pageSize);
    }
    
    @Override
    public int countBySenderIdAndReceiverId(Long senderId, Long receiverId) {
        return messageMapper.countBySenderIdAndReceiverId(senderId, receiverId);
    }

    @Override
    public int countUnreadByUserId(Long userId) {
        return messageMapper.countUnreadByUserId(userId);
    }
}