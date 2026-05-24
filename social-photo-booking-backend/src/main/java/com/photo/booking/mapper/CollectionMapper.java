package com.photo.booking.mapper;

import com.photo.booking.entity.ContentCollection;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollectionMapper {
    @Insert("INSERT INTO collection (user_id, content_id) VALUES (#{userId}, #{contentId})")
    int addCollection(ContentCollection collection);

    @Delete("DELETE FROM collection WHERE user_id = #{userId} AND content_id = #{contentId}")
    int removeCollection(@Param("userId") Long userId, @Param("contentId") Long contentId);

    @Select("SELECT COUNT(*) FROM collection WHERE content_id = #{contentId}")
    int countCollectionsByContentId(Long contentId);

    @Select("SELECT COUNT(*) FROM collection WHERE user_id = #{userId} AND content_id = #{contentId}")
    int checkCollection(@Param("userId") Long userId, @Param("contentId") Long contentId);

    @Select("SELECT c.*, co.title, co.cover_image, co.price, co.location FROM collection c LEFT JOIN content co ON c.content_id = co.id WHERE c.user_id = #{userId} ORDER BY c.create_time DESC")
    List<Map<String, Object>> getCollectionsByUserId(Long userId);
}
