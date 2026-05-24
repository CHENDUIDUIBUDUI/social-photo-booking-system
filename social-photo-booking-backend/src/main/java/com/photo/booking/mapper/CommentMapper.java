package com.photo.booking.mapper;

import com.photo.booking.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("INSERT INTO comment (content_id, user_id, content) VALUES (#{contentId}, #{userId}, #{content})")
    int addComment(Comment comment);

    @Delete("DELETE FROM comment WHERE id = #{id} AND user_id = #{userId}")
    int deleteComment(@Param("id") Long id, @Param("userId") Long userId);

    @Select("SELECT c.*, u.nickname, u.avatar FROM comment c LEFT JOIN user u ON c.user_id = u.id WHERE c.content_id = #{contentId} AND c.status = 1 ORDER BY c.create_time DESC")
    List<Comment> getCommentsByContentId(Long contentId);

    @Select("SELECT COUNT(*) FROM comment WHERE content_id = #{contentId} AND status = 1")
    int countCommentsByContentId(Long contentId);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment getCommentById(Long id);
}
