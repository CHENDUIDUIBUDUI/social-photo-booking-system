package com.photo.booking.service.impl;

import com.photo.booking.entity.CommentLike;
import com.photo.booking.mapper.CommentLikeMapper;
import com.photo.booking.service.CommentLikeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {

    @Resource
    private CommentLikeMapper commentLikeMapper;

    @Override
    public boolean addCommentLike(Long userId, Long commentId) {
        CommentLike commentLike = new CommentLike();
        commentLike.setUserId(userId);
        commentLike.setCommentId(commentId);
        int result = commentLikeMapper.addCommentLike(commentLike);
        return result > 0;
    }

    @Override
    public boolean removeCommentLike(Long userId, Long commentId) {
        int result = commentLikeMapper.removeCommentLike(userId, commentId);
        return result > 0;
    }

    @Override
    public int getCommentLikeCount(Long commentId) {
        return commentLikeMapper.countLikesByCommentId(commentId);
    }

    @Override
    public boolean isCommentLiked(Long userId, Long commentId) {
        int count = commentLikeMapper.checkCommentLike(userId, commentId);
        return count > 0;
    }
}
