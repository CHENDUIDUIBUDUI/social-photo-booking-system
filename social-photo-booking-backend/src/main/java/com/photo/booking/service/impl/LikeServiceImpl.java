package com.photo.booking.service.impl;

import com.photo.booking.entity.Like;
import com.photo.booking.mapper.LikeMapper;
import com.photo.booking.service.LikeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Override
    public boolean addLike(Long userId, Long contentId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setContentId(contentId);
        int result = likeMapper.addLike(like);
        return result > 0;
    }

    @Override
    public boolean removeLike(Long userId, Long contentId) {
        int result = likeMapper.removeLike(userId, contentId);
        return result > 0;
    }

    @Override
    public int getLikeCount(Long contentId) {
        return likeMapper.countLikesByContentId(contentId);
    }

    @Override
    public boolean isLiked(Long userId, Long contentId) {
        int count = likeMapper.checkLike(userId, contentId);
        return count > 0;
    }
}
