package com.photo.booking.mapper;

import com.photo.booking.entity.Content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface ContentMapper {
    @Select("SELECT * FROM content WHERE id = #{id}")
    Content selectById(@Param("id") Long id);
    
    @Insert("INSERT INTO content (user_id, title, description, cover_image, price, location, type, status, view_count, like_count, comment_count, create_time, update_time) VALUES (#{userId}, #{title}, #{description}, #{coverImage}, #{price}, #{location}, #{type}, #{status}, #{viewCount}, #{likeCount}, #{commentCount}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Content content);
    
    @Update("UPDATE content SET user_id = #{userId}, title = #{title}, description = #{description}, cover_image = #{coverImage}, price = #{price}, location = #{location}, type = #{type}, status = #{status}, view_count = #{viewCount}, like_count = #{likeCount}, comment_count = #{commentCount}, update_time = #{updateTime} WHERE id = #{id}")
    int update(Content content);
    
    @Update("UPDATE content SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    @Update("UPDATE content SET view_count = view_count + 1, update_time = NOW() WHERE id = #{id}")
    int updateViewCount(@Param("id") Long id);
    
    @Update("UPDATE content SET like_count = #{count}, update_time = NOW() WHERE id = #{id}")
    int updateLikeCount(@Param("id") Long id, @Param("count") Integer count);
    
    @Update("UPDATE content SET comment_count = #{count}, update_time = NOW() WHERE id = #{id}")
    int updateCommentCount(@Param("id") Long id, @Param("count") Integer count);
    
    @Select("SELECT c.* FROM content c WHERE 1=1 AND (#{type} IS NULL OR c.type = #{type}) AND (#{status} IS NULL OR c.status = #{status}) AND (#{city} IS NULL OR c.location LIKE CONCAT('%', #{city}, '%')) AND (#{tags} IS NULL OR #{tags} = '' OR c.id IN (SELECT ct.content_id FROM content_tag ct WHERE ct.tag_id IN (SELECT id FROM tag WHERE name LIKE CONCAT('%', #{tags}, '%')))) ORDER BY c.create_time DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<Content> selectList(@Param("type") Integer type, @Param("status") Integer status, @Param("tags") String tags, @Param("city") String city, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);
    
    @Select("SELECT * FROM content WHERE user_id = #{userId} AND (#{type} IS NULL OR type = #{type}) ORDER BY create_time DESC")
    List<Content> selectByUserId(@Param("userId") Long userId, @Param("type") Integer type);
    
    @Select("SELECT * FROM content WHERE status = 0 ORDER BY create_time DESC")
    List<Content> selectPendingReview();
    
    @Select("SELECT * FROM content WHERE status = 1 ORDER BY (view_count * 0.4 + comment_count * 0.3 + like_count * 0.3) DESC LIMIT #{limit}")
    List<Content> selectHot(@Param("limit") Integer limit);
    
    @Select("SELECT * FROM content WHERE status = 1 ORDER BY create_time DESC")
    List<Content> selectAll();
    
    // 查询作品的标签列表
    @Select("SELECT t.name FROM tag t INNER JOIN content_tag ct ON t.id = ct.tag_id WHERE ct.content_id = #{contentId} AND t.status = 1")
    List<String> selectTagsByContentId(@Param("contentId") Long contentId);
}
