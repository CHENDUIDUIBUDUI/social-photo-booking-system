package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Like;
import com.socialshoot.admin.mapper.LikeMapper;
import com.socialshoot.admin.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    
    @Autowired
    private LikeMapper likeMapper;
    
    @Override
    public void addLike(Long userId, Long contentId, Integer type) {
        Like like = new Like();
        like.setUserId(userId);
        like.setContentId(contentId);
        like.setType(type);
        like.setCreateTime(LocalDateTime.now());
        likeMapper.insert(like);
    }
    
    @Override
    public void removeLike(Long userId, Long contentId, Integer type) {
        likeMapper.delete(userId, contentId, type);
    }
    
    @Override
    public boolean isLiked(Long userId, Long contentId, Integer type) {
        return likeMapper.findByUserIdAndContentId(userId, contentId, type) != null;
    }
    
    @Override
    public int getLikeCount(Long contentId, Integer type) {
        return likeMapper.countByContentId(contentId, type);
    }
    
    @Override
    public List<Like> getLikesByUserId(Long userId, Integer type) {
        return likeMapper.findByUserId(userId, type);
    }
}