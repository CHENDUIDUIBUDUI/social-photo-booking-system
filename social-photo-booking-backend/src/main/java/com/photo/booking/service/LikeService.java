package com.photo.booking.service;

public interface LikeService {
    boolean addLike(Long userId, Long contentId);
    boolean removeLike(Long userId, Long contentId);
    int getLikeCount(Long contentId);
    boolean isLiked(Long userId, Long contentId);
}
