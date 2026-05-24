package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Like;

import java.util.List;

public interface LikeService {
    void addLike(Long userId, Long contentId, Integer type);
    void removeLike(Long userId, Long contentId, Integer type);
    boolean isLiked(Long userId, Long contentId, Integer type);
    int getLikeCount(Long contentId, Integer type);
    List<Like> getLikesByUserId(Long userId, Integer type);
}