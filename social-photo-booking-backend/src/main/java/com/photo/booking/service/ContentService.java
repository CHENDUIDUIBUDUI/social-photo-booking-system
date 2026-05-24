package com.photo.booking.service;

import com.photo.booking.entity.Content;

import java.util.List;

public interface ContentService {
    Content createContent(Content content);
    Content getContentById(Long id);
    Content updateContent(Content content);
    void updateContentStatus(Long id, Integer status);
    List<Content> getContentList(Integer type, Integer status, String tags, String city, Integer page, Integer pageSize, Integer offset);
    List<Content> getContentByUserId(Long userId, Integer type);
    List<Content> getPendingReview();
    List<Content> getHotContents(Integer limit);
    List<Content> getAllContents();
}
