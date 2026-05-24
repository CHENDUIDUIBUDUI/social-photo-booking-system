package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Collection;
import com.socialshoot.admin.mapper.CollectionMapper;
import com.socialshoot.admin.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {
    
    @Autowired
    private CollectionMapper collectionMapper;
    
    @Override
    public List<Collection> findByUserId(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return collectionMapper.findByUserId(userId, offset, pageSize);
    }
    
    @Override
    public int countByUserId(Long userId) {
        return collectionMapper.countByUserId(userId);
    }
    
    @Override
    public Collection findByUserIdAndContentId(Long userId, Long contentId) {
        return collectionMapper.findByUserIdAndContentId(userId, contentId);
    }
    
    @Override
    public boolean isCollected(Long userId, Long contentId) {
        Collection collection = collectionMapper.findByUserIdAndContentId(userId, contentId);
        return collection != null;
    }
}