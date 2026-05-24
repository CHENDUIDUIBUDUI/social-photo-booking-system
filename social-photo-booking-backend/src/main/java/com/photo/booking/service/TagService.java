package com.photo.booking.service;

import com.photo.booking.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();
    List<Tag> getTagsByType(Integer type);
}