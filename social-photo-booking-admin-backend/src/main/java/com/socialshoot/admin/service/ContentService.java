package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Content;

import java.util.List;

public interface ContentService {
    List<Content> findAll(int page, int pageSize);
    List<Content> search(String id, String title, String type, String status, int page, int pageSize);
    int countSearch(String id, String title, String type, String status);
    Content findById(Long id);
    List<Content> findByUserId(Long userId, int page, int pageSize);
    int countByUserId(Long userId);
    int countAll();
    void save(Content content);
    void deleteById(Long id);
    List<Content> findPending(int page, int pageSize);
    int countPending();
    void approveContent(Long id);
    void rejectContent(Long id, String reason);
}
