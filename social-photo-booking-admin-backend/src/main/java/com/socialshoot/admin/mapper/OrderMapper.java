package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    
    @Select("SELECT * FROM `order` WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Order> findByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT * FROM `order` WHERE photographer_id = #{photographerId} ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Order> findByPhotographerId(@Param("photographerId") Long photographerId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT * FROM `order` WHERE id = #{id}")
    Order findById(@Param("id") Long id);
    
    @Select("SELECT COUNT(*) FROM `order` WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM `order` WHERE photographer_id = #{photographerId}")
    int countByPhotographerId(@Param("photographerId") Long photographerId);
    
    @Update("UPDATE `order` SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    @Select("SELECT COUNT(*) FROM `order`")
    int countAllOrders();
    
    @Select("SELECT SUM(total_amount) FROM `order`")
    Double sumTotalAmount();
    
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m') as month, COUNT(*) as count FROM `order` GROUP BY DATE_FORMAT(create_time, '%Y-%m') ORDER BY month LIMIT 6")
    List<Map<String, Object>> getOrderTrend();
    
    @Select("${sql}")
    List<Order> selectOrdersByCondition(@Param("sql") String sql);
    
    @Select("${sql}")
    int countOrdersByCondition(@Param("sql") String sql);
    
    @Update("UPDATE `order` SET order_no = #{order_no}, user_id = #{user_id}, photographer_id = #{photographer_id}, total_amount = #{total_amount}, deposit_amount = #{deposit_amount}, paid_balance = #{paid_balance}, shoot_time = #{shoot_time}, location = #{location}, status = #{status}, notes = #{notes}, update_time = NOW() WHERE id = #{id}")
    int updateOrder(Order order);
    
    @Delete("DELETE FROM `order` WHERE id = #{id}")
    int deleteOrder(@Param("id") Long id);
    
    @Update("UPDATE `order` SET user_rating = #{rating}, user_comment = #{comment}, update_time = NOW() WHERE id = #{orderId}")
    int updateUserReview(@Param("orderId") Long orderId, @Param("rating") Integer rating, @Param("comment") String comment);
    
    @Update("UPDATE `order` SET photographer_rating = #{rating}, photographer_comment = #{comment}, update_time = NOW() WHERE id = #{orderId}")
    int updatePhotographerReview(@Param("orderId") Long orderId, @Param("rating") Integer rating, @Param("comment") String comment);
    
    @Select("SELECT * FROM `order` WHERE user_id = #{userId} AND photographer_rating IS NOT NULL ORDER BY create_time DESC")
    List<Order> findUserReviews(@Param("userId") Long userId);
    
    @Select("SELECT * FROM `order` WHERE photographer_id = #{photographerId} AND user_rating IS NOT NULL ORDER BY create_time DESC")
    List<Order> findPhotographerReviews(@Param("photographerId") Long photographerId);
}