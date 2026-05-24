package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.ModelCard;
import com.socialshoot.admin.mapper.ModelCardMapper;
import com.socialshoot.admin.service.ModelCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelCardServiceImpl implements ModelCardService {

    @Autowired
    private ModelCardMapper modelCardMapper;

    @Override
    public void save(ModelCard modelCard) {
        if (modelCard.getId() != null) {
            modelCardMapper.update(modelCard);
        } else {
            modelCardMapper.insert(modelCard);
        }
    }

    @Override
    public ModelCard findByUserId(Long userId) {
        return modelCardMapper.findByUserId(userId);
    }

    @Override
    public ModelCard findById(Long id) {
        return modelCardMapper.findById(id);
    }

    @Override
    public List<ModelCard> findAll(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return modelCardMapper.findAll(offset, pageSize);
    }

    @Override
    public int countAll() {
        return modelCardMapper.countAll();
    }

    @Override
    public List<ModelCard> findPending(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return modelCardMapper.findPending(offset, pageSize);
    }

    @Override
    public int countPending() {
        return modelCardMapper.countPending();
    }

    @Override
    public void approve(Long id, Long auditorId) {
        modelCardMapper.approve(id, auditorId);
    }

    @Override
    public void reject(Long id, String rejectReason, Long auditorId) {
        modelCardMapper.reject(id, rejectReason, auditorId);
    }
}