package com.photo.booking.service;

import java.util.List;
import java.util.Map;

public interface CollectionService {
    boolean addCollection(Long userId, Long contentId);
    boolean removeCollection(Long userId, Long contentId);
    int getCollectionCount(Long contentId);
    boolean isCollected(Long userId, Long contentId);
    List<Map<String, Object>> getCollectionsByUserId(Long userId);
}
