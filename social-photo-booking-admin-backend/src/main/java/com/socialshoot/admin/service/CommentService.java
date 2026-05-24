package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);
    void deleteComment(Long id);
    void likeComment(Long id);
    List<Comment> getCommentsByContentId(Long contentId);
    List<Comment> getCommentsByUserId(Long userId);
    List<Comment> getRepliesByParentId(Long parentId);
}