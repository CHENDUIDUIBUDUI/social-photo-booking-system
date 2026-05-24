package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Collection;

import java.util.List;

public interface CollectionService {
    List<Collection> findByUserId(Long userId, int page, int pageSize);
    int countByUserId(Long userId);
    Collection findByUserIdAndContentId(Long userId, Long contentId);
    boolean isCollected(Long userId, Long contentId);
}