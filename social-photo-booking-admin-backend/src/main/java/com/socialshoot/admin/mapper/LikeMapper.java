package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Like;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LikeMapper {
    
    @Insert("INSERT INTO `like` (user_id, content_id, type, create_time) VALUES (#{userId}, #{contentId}, #{type}, #{createTime})")
    int insert(Like like);
    
    @Delete("DELETE FROM `like` WHERE user_id = #{userId} AND content_id = #{contentId} AND type = #{type}")
    int delete(@Param("userId") Long userId, @Param("contentId") Long contentId, @Param("type") Integer type);
    
    @Select("SELECT * FROM `like` WHERE user_id = #{userId} AND content_id = #{contentId} AND type = #{type}")
    Like findByUserIdAndContentId(@Param("userId") Long userId, @Param("contentId") Long contentId, @Param("type") Integer type);
    
    @Select("SELECT COUNT(*) FROM `like` WHERE content_id = #{contentId} AND type = #{type}")
    int countByContentId(@Param("contentId") Long contentId, @Param("type") Integer type);
    
    @Select("SELECT * FROM `like` WHERE user_id = #{userId} AND type = #{type} ORDER BY create_time DESC")
    List<Like> findByUserId(@Param("userId") Long userId, @Param("type") Integer type);
}