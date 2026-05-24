package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Follow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowMapper {
    
    @Select("SELECT f.*, u.nickname, u.avatar, u.bio " +
            "FROM follow f " +
            "LEFT JOIN user u ON f.follow_user_id = u.id " +
            "WHERE f.user_id = #{userId} ORDER BY f.create_time DESC LIMIT #{offset}, #{limit}")
    List<Follow> findByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM follow WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM follow WHERE user_id = #{userId} AND follow_user_id = #{followUserId}")
    Follow findByUserIdAndFollowUserId(@Param("userId") Long userId, @Param("followUserId") Long followUserId);
    
    @Delete("DELETE FROM follow WHERE user_id = #{userId} AND follow_user_id = #{followUserId}")
    int deleteByUserIdAndFollowUserId(@Param("userId") Long userId, @Param("followUserId") Long followUserId);
}