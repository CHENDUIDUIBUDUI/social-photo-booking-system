package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnnouncementMapper {
    @Select("SELECT id, title, content, author_id as authorId, status, create_time as createTime, update_time as updateTime FROM announcement ORDER BY create_time DESC LIMIT #{pageNum}, #{pageSize}")
    List<Announcement> selectAnnouncements(Map<String, Object> params);
    
    @Select("SELECT COUNT(*) FROM announcement")
    int countAnnouncements(Map<String, Object> params);
    
    @Select("SELECT id, title, content, author_id as authorId, status, create_time as createTime, update_time as updateTime FROM announcement WHERE id = #{id}")
    Announcement selectAnnouncementById(Long id);
    
    @Insert("INSERT INTO announcement (title, content, author_id, status, create_time, update_time) VALUES (#{title}, #{content}, #{authorId}, #{status}, NOW(), NOW())")
    int insertAnnouncement(Announcement announcement);
    
    @Update("UPDATE announcement SET title = #{title}, content = #{content}, author_id = #{authorId}, status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateAnnouncement(Announcement announcement);
    
    @Delete("DELETE FROM announcement WHERE id = #{id}")
    int deleteAnnouncement(Long id);
}
