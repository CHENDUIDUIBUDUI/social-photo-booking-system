package com.photo.booking.mapper;

import com.photo.booking.entity.User;
import com.photo.booking.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectByOpenid(@Param("openid") String openid);
    User selectByPhone(@Param("phone") String phone);
    User selectById(@Param("id") Long id);
    int insert(User user);
    int update(User user);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int updateCreditScore(@Param("id") Long id, @Param("creditScore") Integer creditScore);
    int updateRole(@Param("id") Long id, @Param("role") Integer role);
    int deleteByPhone(@Param("phone") String phone);
    List<User> selectList(@Param("role") Integer role, @Param("status") Integer status);
    
    // 用户资料相关
    UserProfile selectUserProfileByUserId(@Param("userId") Long userId);
    int insertUserProfile(UserProfile userProfile);
    int updateUserProfile(UserProfile userProfile);
}
