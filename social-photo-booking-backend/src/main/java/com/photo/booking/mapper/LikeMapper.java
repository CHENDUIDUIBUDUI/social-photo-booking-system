package com.photo.booking.mapper;

import com.photo.booking.entity.Like;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LikeMapper {
    @Insert("INSERT INTO `like` (user_id, content_id) VALUES (#{userId}, #{contentId})")
    int addLike(Like like);

    @Delete("DELETE FROM `like` WHERE user_id = #{userId} AND content_id = #{contentId}")
    int removeLike(@Param("userId") Long userId, @Param("contentId") Long contentId);

    @Select("SELECT COUNT(*) FROM `like` WHERE content_id = #{contentId}")
    int countLikesByContentId(Long contentId);

    @Select("SELECT COUNT(*) FROM `like` WHERE user_id = #{userId} AND content_id = #{contentId}")
    int checkLike(@Param("userId") Long userId, @Param("contentId") Long contentId);
}
