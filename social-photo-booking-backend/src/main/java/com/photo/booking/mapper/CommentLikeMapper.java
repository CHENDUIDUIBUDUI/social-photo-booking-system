package com.photo.booking.mapper;

import com.photo.booking.entity.CommentLike;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CommentLikeMapper {
    @Insert("INSERT INTO comment_like (user_id, comment_id) VALUES (#{userId}, #{commentId})")
    int addCommentLike(CommentLike commentLike);

    @Delete("DELETE FROM comment_like WHERE user_id = #{userId} AND comment_id = #{commentId}")
    int removeCommentLike(@Param("userId") Long userId, @Param("commentId") Long commentId);

    @Select("SELECT COUNT(*) FROM comment_like WHERE comment_id = #{commentId}")
    int countLikesByCommentId(Long commentId);

    @Select("SELECT COUNT(*) FROM comment_like WHERE user_id = #{userId} AND comment_id = #{commentId}")
    int checkCommentLike(@Param("userId") Long userId, @Param("commentId") Long commentId);
}
