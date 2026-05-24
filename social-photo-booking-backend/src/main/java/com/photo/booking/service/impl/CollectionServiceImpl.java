package com.photo.booking.service.impl;

import com.photo.booking.entity.ContentCollection;
import com.photo.booking.mapper.CollectionMapper;
import com.photo.booking.service.CollectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Resource
    private CollectionMapper collectionMapper;

    @Override
    public boolean addCollection(Long userId, Long contentId) {
        ContentCollection collection = new ContentCollection();
        collection.setUserId(userId);
        collection.setContentId(contentId);
        int result = collectionMapper.addCollection(collection);
        return result > 0;
    }

    @Override
    public boolean removeCollection(Long userId, Long contentId) {
        int result = collectionMapper.removeCollection(userId, contentId);
        return result > 0;
    }

    @Override
    public int getCollectionCount(Long contentId) {
        return collectionMapper.countCollectionsByContentId(contentId);
    }

    @Override
    public boolean isCollected(Long userId, Long contentId) {
        int count = collectionMapper.checkCollection(userId, contentId);
        return count > 0;
    }

    @Override
    public List<Map<String, Object>> getCollectionsByUserId(Long userId) {
        return collectionMapper.getCollectionsByUserId(userId);
    }
}
