package com.photo.booking.mapper;

import com.photo.booking.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    Order selectById(@Param("id") Long id);
    Order selectByOrderNo(@Param("orderNo") String orderNo);
    int insert(Order order);
    int update(Order order);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int updateRating(@Param("id") Long id, @Param("userRating") Integer userRating, @Param("userComment") String userComment, @Param("photographerRating") Integer photographerRating, @Param("photographerComment") String photographerComment);
    List<Order> selectByUserId(@Param("userId") Long userId, @Param("status") Integer status);
    List<Order> selectByPhotographerId(@Param("photographerId") Long photographerId, @Param("status") Integer status);
    List<Order> selectList(@Param("status") Integer status);
}
