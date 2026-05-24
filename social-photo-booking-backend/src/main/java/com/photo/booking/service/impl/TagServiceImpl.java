package com.photo.booking.service.impl;

import com.photo.booking.entity.Tag;
import com.photo.booking.mapper.TagMapper;
import com.photo.booking.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.selectAll();
    }

    @Override
    public List<Tag> getTagsByType(Integer type) {
        return tagMapper.selectByType(type);
    }
}