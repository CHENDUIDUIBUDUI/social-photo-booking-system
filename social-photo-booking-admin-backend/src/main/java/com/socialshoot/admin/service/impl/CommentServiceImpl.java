package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Comment;
import com.socialshoot.admin.mapper.CommentMapper;
import com.socialshoot.admin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Override
    public void addComment(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setLikes(0);
        commentMapper.insert(comment);
    }
    
    @Override
    public void deleteComment(Long id) {
        commentMapper.delete(id);
    }
    
    @Override
    public void likeComment(Long id) {
        commentMapper.incrementLikes(id);
    }
    
    @Override
    public List<Comment> getCommentsByContentId(Long contentId) {
        return commentMapper.findByContentId(contentId);
    }
    
    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentMapper.findByUserId(userId);
    }
    
    @Override
    public List<Comment> getRepliesByParentId(Long parentId) {
        return commentMapper.findByParentId(parentId);
    }
}