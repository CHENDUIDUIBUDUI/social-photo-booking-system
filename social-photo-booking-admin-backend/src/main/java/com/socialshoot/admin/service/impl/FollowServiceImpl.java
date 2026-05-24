package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Follow;
import com.socialshoot.admin.mapper.FollowMapper;
import com.socialshoot.admin.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    
    @Autowired
    private FollowMapper followMapper;
    
    @Override
    public List<Follow> findByUserId(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return followMapper.findByUserId(userId, offset, pageSize);
    }
    
    @Override
    public int countByUserId(Long userId) {
        return followMapper.countByUserId(userId);
    }
    
    @Override
    public Follow findByUserIdAndFollowUserId(Long userId, Long followUserId) {
        return followMapper.findByUserIdAndFollowUserId(userId, followUserId);
    }
}