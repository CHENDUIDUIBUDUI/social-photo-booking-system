package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Content;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContentMapper {
    
    @Select("SELECT id, user_id as userId, create_time as createTime, title, description, cover_image as coverImage, location, location_id as locationId, price_type as priceType, price, deposit, type, status, audit_reason as auditReason, view_count as viewCount, like_count as likeCount, comment_count as commentCount, update_time as updateTime FROM content ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Content> findAll(@Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT id, user_id as userId, create_time as createTime, title, description, cover_image as coverImage, location, location_id as locationId, price_type as priceType, price, deposit, type, status, audit_reason as auditReason, view_count as viewCount, like_count as likeCount, comment_count as commentCount, update_time as updateTime FROM content WHERE id = #{id}")
    Content findById(@Param("id") Long id);

    @Select("SELECT id, user_id as userId, create_time as createTime, title, description, cover_image as coverImage, location, location_id as locationId, price_type as priceType, price, deposit, type, status, audit_reason as auditReason, view_count as viewCount, like_count as likeCount, comment_count as commentCount, update_time as updateTime FROM content WHERE id = #{id}")
    Content selectById(@Param("id") Long id);
    
    @Select("SELECT id, user_id as userId, create_time as createTime, title, description, cover_image as coverImage, location, location_id as locationId, price_type as priceType, price, deposit, type, status, audit_reason as auditReason, view_count as viewCount, like_count as likeCount, comment_count as commentCount, update_time as updateTime FROM content WHERE user_id = #{userId} AND status = 1 ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Content> findByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM content WHERE user_id = #{userId} AND status = 1")
    int countByUserId(@Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM content")
    int countAll();
    
    @Select({"<script>",
            "SELECT id, user_id as userId, create_time as createTime, title, description, cover_image as coverImage, location, location_id as locationId, price_type as priceType, price, deposit, type, status, audit_reason as auditReason, view_count as viewCount, like_count as likeCount, comment_count as commentCount, update_time as updateTime FROM content",
            "<where>",
            "<if test='id != null and id != \"\"'>AND id = #{id}</if>",
            "<if test='title != null and title != \"\"'>AND title LIKE CONCAT('%', #{title}, '%')</if>",
            "<if test='type != null and type != \"\"'>AND type = #{type}</if>",
            "<if test='status != null and status != \"\"'>AND status = #{status}</if>",
            "</where>",
            "ORDER BY create_time DESC LIMIT #{offset}, #{limit}",
            "</script>"})
    List<Content> search(@Param("id") String id, @Param("title") String title, @Param("type") String type, @Param("status") String status, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select({"<script>",
            "SELECT COUNT(*) FROM content",
            "<where>",
            "<if test='id != null and id != \"\"'>AND id = #{id}</if>",
            "<if test='title != null and title != \"\"'>AND title LIKE CONCAT('%', #{title}, '%')</if>",
            "<if test='type != null and type != \"\"'>AND type = #{type}</if>",
            "<if test='status != null and status != \"\"'>AND status = #{status}</if>",
            "</where>",
            "</script>"})
    int countSearch(@Param("id") String id, @Param("title") String title, @Param("type") String type, @Param("status") String status);
    
    @Insert("INSERT INTO content (user_id, title, description, location, location_id, price, type, status, create_time, update_time) " +
            "VALUES (#{userId}, #{title}, #{description}, #{location}, #{locationId}, #{price}, #{type}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Content content);
    
    @Update("UPDATE content SET title = #{title}, description = #{description}, location = #{location}, location_id = #{locationId}, " +
            "price = #{price}, type = #{type}, status = #{status}, update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void update(Content content);
    
    @Update("UPDATE content SET status = 0 WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
    
    // 待审核内容查询
    @Select("SELECT id, user_id as userId, create_time as createTime, title, description, cover_image as coverImage, location, location_id as locationId, price_type as priceType, price, deposit, type, status, audit_reason as auditReason, view_count as viewCount, like_count as likeCount, comment_count as commentCount, update_time as updateTime FROM content WHERE status = 0 ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Content> findPending(@Param("offset") int offset, @Param("limit") int pageSize);
    
    // 待审核内容数量
    @Select("SELECT COUNT(*) FROM content WHERE status = 0")
    int countPending();
    
    // 审核通过
    @Update("UPDATE content SET status = 1, update_time = NOW() WHERE id = #{id}")
    void approveContent(@Param("id") Long id);
    
    // 审核拒绝
    @Update("UPDATE content SET status = 2, audit_reason = #{reason}, update_time = NOW() WHERE id = #{id}")
    void rejectContent(@Param("id") Long id, @Param("reason") String reason);
}
