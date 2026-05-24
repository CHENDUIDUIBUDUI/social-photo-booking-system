package com.photo.booking.service.impl;

import com.photo.booking.entity.Comment;
import com.photo.booking.mapper.CommentMapper;
import com.photo.booking.service.CommentService;
import com.photo.booking.service.CommentLikeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private CommentLikeService commentLikeService;

    @Override
    public boolean addComment(Long userId, Long contentId, String content) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContentId(contentId);
        comment.setContent(content);
        comment.setStatus(1);
        int result = commentMapper.addComment(comment);
        return result > 0;
    }

    @Override
    public boolean deleteComment(Long userId, Long commentId) {
        int result = commentMapper.deleteComment(commentId, userId);
        return result > 0;
    }

    @Override
    public List<Comment> getCommentsByContentId(Long contentId, Long userId) {
        List<Comment> comments = commentMapper.getCommentsByContentId(contentId);
        // 为每个评论添加点赞信息
        for (Comment comment : comments) {
            comment.setLikeCount(commentLikeService.getCommentLikeCount(comment.getId()));
            if (userId != null) {
                comment.setLiked(commentLikeService.isCommentLiked(userId, comment.getId()));
            }
        }
        return comments;
    }

    @Override
    public int getCommentCount(Long contentId) {
        return commentMapper.countCommentsByContentId(contentId);
    }
}
