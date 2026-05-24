package com.photo.booking.mapper;

import com.photo.booking.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    int insert(Message message);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int updateStatusByReceiveUserId(@Param("receiveUserId") Long receiveUserId, @Param("status") Integer status);
    List<Message> selectByReceiveUserId(@Param("receiveUserId") Long receiveUserId, @Param("type") Integer type);
    List<Message> selectChatMessages(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    int countUnreadByUserId(@Param("userId") Long userId);
}
