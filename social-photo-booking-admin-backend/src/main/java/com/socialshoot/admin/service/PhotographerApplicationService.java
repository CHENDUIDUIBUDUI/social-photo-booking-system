package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.PhotographerApplication;

import java.util.List;

public interface PhotographerApplicationService {
    void save(PhotographerApplication application);
    PhotographerApplication findByUserId(Long userId);
    PhotographerApplication findById(Long id);
    List<PhotographerApplication> findPending(int page, int pageSize);
    int countPending();
    void approve(Long id, Long auditorId);
    void reject(Long id, String rejectReason, Long auditorId);
}