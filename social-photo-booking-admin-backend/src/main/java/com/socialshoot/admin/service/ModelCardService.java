package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.ModelCard;

import java.util.List;

public interface ModelCardService {
    void save(ModelCard modelCard);
    ModelCard findByUserId(Long userId);
    ModelCard findById(Long id);
    List<ModelCard> findAll(int page, int pageSize);
    int countAll();
    List<ModelCard> findPending(int page, int pageSize);
    int countPending();
    void approve(Long id, Long auditorId);
    void reject(Long id, String rejectReason, Long auditorId);
}