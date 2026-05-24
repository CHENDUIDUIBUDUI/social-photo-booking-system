package com.photo.booking.service;

import com.photo.booking.entity.Comment;

import java.util.List;

public interface CommentService {
    boolean addComment(Long userId, Long contentId, String content);
    boolean deleteComment(Long userId, Long commentId);
    List<Comment> getCommentsByContentId(Long contentId, Long userId);
    int getCommentCount(Long contentId);
}
