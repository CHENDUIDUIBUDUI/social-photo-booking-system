package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    
    @Insert("INSERT INTO comment (user_id, content_id, content, parent_id, create_time) VALUES (#{userId}, #{contentId}, #{content}, #{parentId}, #{createTime})")
    int insert(Comment comment);
    
    @Delete("DELETE FROM comment WHERE id = #{id}")
    int delete(@Param("id") Long id);
    
    @Update("UPDATE comment SET likes = likes + 1 WHERE id = #{id}")
    int incrementLikes(@Param("id") Long id);
    
    @Select("SELECT * FROM comment WHERE content_id = #{contentId} ORDER BY create_time DESC")
    List<Comment> findByContentId(@Param("contentId") Long contentId);
    
    @Select("SELECT * FROM comment WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Comment> findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM comment WHERE parent_id = #{parentId} ORDER BY create_time ASC")
    List<Comment> findByParentId(@Param("parentId") Long parentId);
}