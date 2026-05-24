package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Content;
import com.socialshoot.admin.mapper.ContentMapper;
import com.socialshoot.admin.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    
    @Autowired
    private ContentMapper contentMapper;
    
    @Override
    public List<Content> findAll(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return contentMapper.findAll(offset, pageSize);
    }
    
    @Override
    public List<Content> search(String id, String title, String type, String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        // 处理空字符串，转换为 null
        id = (id != null && id.trim().isEmpty()) ? null : id;
        title = (title != null && title.trim().isEmpty()) ? null : title;
        type = (type != null && type.trim().isEmpty()) ? null : type;
        status = (status != null && status.trim().isEmpty()) ? null : status;
        return contentMapper.search(id, title, type, status, offset, pageSize);
    }
    
    @Override
    public int countSearch(String id, String title, String type, String status) {
        // 处理空字符串，转换为 null
        id = (id != null && id.trim().isEmpty()) ? null : id;
        title = (title != null && title.trim().isEmpty()) ? null : title;
        type = (type != null && type.trim().isEmpty()) ? null : type;
        status = (status != null && status.trim().isEmpty()) ? null : status;
        return contentMapper.countSearch(id, title, type, status);
    }
    
    @Override
    public Content findById(Long id) {
        return contentMapper.findById(id);
    }
    
    @Override
    public List<Content> findByUserId(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return contentMapper.findByUserId(userId, offset, pageSize);
    }
    
    @Override
    public int countByUserId(Long userId) {
        return contentMapper.countByUserId(userId);
    }
    
    @Override
    public int countAll() {
        return contentMapper.countAll();
    }
    
    @Override
    public void save(Content content) {
        if (content.getId() != null) {
            contentMapper.update(content);
        } else {
            contentMapper.insert(content);
        }
    }
    
    @Override
    public void deleteById(Long id) {
        contentMapper.deleteById(id);
    }
    
    @Override
    public List<Content> findPending(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return contentMapper.findPending(offset, pageSize);
    }
    
    @Override
    public int countPending() {
        return contentMapper.countPending();
    }
    
    @Override
    public void approveContent(Long id) {
        contentMapper.approveContent(id);
    }
    
    @Override
    public void rejectContent(Long id, String reason) {
        contentMapper.rejectContent(id, reason);
    }
}
