package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.PhotographerApplication;
import com.socialshoot.admin.mapper.PhotographerApplicationMapper;
import com.socialshoot.admin.mapper.UserMapper;
import com.socialshoot.admin.service.PhotographerApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhotographerApplicationServiceImpl implements PhotographerApplicationService {

    @Autowired
    private PhotographerApplicationMapper photographerApplicationMapper;

    @Override
    public void save(PhotographerApplication application) {
        if (application.getId() != null) {
            photographerApplicationMapper.update(application);
        } else {
            photographerApplicationMapper.insert(application);
        }
    }

    @Override
    public PhotographerApplication findByUserId(Long userId) {
        return photographerApplicationMapper.findByUserId(userId);
    }

    @Override
    public PhotographerApplication findById(Long id) {
        return photographerApplicationMapper.findById(id);
    }

    @Override
    public List<PhotographerApplication> findPending(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return photographerApplicationMapper.findPending(offset, pageSize);
    }

    @Override
    public int countPending() {
        return photographerApplicationMapper.countPending();
    }

    @Override
    public void approve(Long id, Long auditorId) {
        photographerApplicationMapper.approve(id, auditorId, LocalDateTime.now());
    }

    @Override
    public void reject(Long id, String rejectReason, Long auditorId) {
        photographerApplicationMapper.reject(id, rejectReason, auditorId, LocalDateTime.now());
    }
}