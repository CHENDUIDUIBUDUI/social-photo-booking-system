package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Follow;

import java.util.List;

public interface FollowService {
    List<Follow> findByUserId(Long userId, int page, int pageSize);
    int countByUserId(Long userId);
    Follow findByUserIdAndFollowUserId(Long userId, Long followUserId);
}