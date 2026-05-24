package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MessageMapper {
    
    @Select("SELECT m.*, u.nickname as senderName, u.avatar as senderAvatar " +
            "FROM message m " +
            "LEFT JOIN user u ON m.sender_id = u.id " +
            "WHERE (m.sender_id = #{userId} OR m.receiver_id = #{userId}) " +
            "ORDER BY m.create_time DESC LIMIT #{offset}, #{limit}")
    List<Message> findByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM message WHERE (sender_id = #{userId} OR receiver_id = #{userId})")
    int countByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM message WHERE sender_id = #{senderId} AND receiver_id = #{receiverId} ORDER BY create_time ASC LIMIT #{offset}, #{limit}")
    List<Message> findBySenderIdAndReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM message WHERE sender_id = #{senderId} AND receiver_id = #{receiverId}")
    int countBySenderIdAndReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    
    @Insert("INSERT INTO message (sender_id, receiver_id, content, is_read, create_time) " +
            "VALUES (#{senderId}, #{receiverId}, #{content}, #{isRead}, #{createTime})")
    int insert(Message message);
    
    @Update("UPDATE message SET is_read = #{isRead} WHERE id = #{id}")
    int updateReadStatus(@Param("id") Long id, @Param("isRead") Integer isRead);
    
    @Select("SELECT COUNT(*) FROM message WHERE receiver_id = #{userId} AND is_read = 0")
    int countUnreadByUserId(@Param("userId") Long userId);
}