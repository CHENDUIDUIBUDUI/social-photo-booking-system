package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Photographer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface PhotographerMapper {
    
    @Insert("INSERT INTO photographer (user_id, name, avatar, certified, certified_time, orders, rating, price, region, styles, cover_image, bio, works, status, create_time, update_time) " +
            "VALUES (#{userId}, #{name}, #{avatar}, #{certified}, #{certifiedTime}, #{orders}, #{rating}, #{price}, #{region}, #{styles}, #{coverImage}, #{bio}, #{works}, #{status}, #{createTime}, #{updateTime})")
    void insert(Photographer photographer);
}