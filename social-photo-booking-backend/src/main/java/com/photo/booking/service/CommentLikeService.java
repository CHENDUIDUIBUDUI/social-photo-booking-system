package com.photo.booking.service;

public interface CommentLikeService {
    boolean addCommentLike(Long userId, Long commentId);
    boolean removeCommentLike(Long userId, Long commentId);
    int getCommentLikeCount(Long commentId);
    boolean isCommentLiked(Long userId, Long commentId);
}
