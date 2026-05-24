package com.photo.booking.mapper;

import com.photo.booking.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {
    List<Tag> selectAll();
    List<Tag> selectByType(Integer type);
}