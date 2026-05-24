package com.photo.booking.service.impl;

import com.photo.booking.entity.Content;
import com.photo.booking.mapper.ContentMapper;
import com.photo.booking.service.ContentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Resource
    private ContentMapper contentMapper;

    @Override
    public Content createContent(Content content) {
        contentMapper.insert(content);
        return content;
    }

    @Override
    public Content getContentById(Long id) {
        Content content = contentMapper.selectById(id);
        if (content != null) {
            contentMapper.updateViewCount(id);
            // 获取作品的标签列表
            List<String> tags = contentMapper.selectTagsByContentId(id);
            content.setTagList(tags);
        }
        return content;
    }

    @Override
    public Content updateContent(Content content) {
        contentMapper.update(content);
        return content;
    }

    @Override
    public void updateContentStatus(Long id, Integer status) {
        contentMapper.updateStatus(id, status);
    }

    @Override
    public List<Content> getContentList(Integer type, Integer status, String tags, String city, Integer page, Integer pageSize, Integer offset) {
        return contentMapper.selectList(type, status, tags, city, pageSize, offset);
    }

    @Override
    public List<Content> getContentByUserId(Long userId, Integer type) {
        return contentMapper.selectByUserId(userId, type);
    }

    @Override
    public List<Content> getPendingReview() {
        return contentMapper.selectPendingReview();
    }
    
    @Override
    public List<Content> getHotContents(Integer limit) {
        return contentMapper.selectHot(limit);
    }
    
    @Override
    public List<Content> getAllContents() {
        return contentMapper.selectAll();
    }
}
