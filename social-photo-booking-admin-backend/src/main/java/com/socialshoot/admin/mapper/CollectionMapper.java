package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Collection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollectionMapper {
    
    @Select("SELECT c.*, u.nickname as author, u.avatar as authorAvatar, c1.title, c1.imageUrl, c1.likes, c1.comments " +
            "FROM collection c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "LEFT JOIN content c1 ON c.content_id = c1.id " +
            "WHERE c.user_id = #{userId} ORDER BY c.create_time DESC LIMIT #{offset}, #{limit}")
    List<Collection> findByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM collection WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM collection WHERE user_id = #{userId} AND content_id = #{contentId}")
    Collection findByUserIdAndContentId(@Param("userId") Long userId, @Param("contentId") Long contentId);
    
    @Delete("DELETE FROM collection WHERE user_id = #{userId} AND content_id = #{contentId}")
    int deleteByUserIdAndContentId(@Param("userId") Long userId, @Param("contentId") Long contentId);
}