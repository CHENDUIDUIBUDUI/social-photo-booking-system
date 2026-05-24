package com.photo.booking.service;

import com.photo.booking.entity.User;
import com.photo.booking.entity.UserProfile;

public interface UserService {
    User login(String openid);
    User login(String openid, String avatarUrl, String nickName);
    User loginByPhone(String phoneNumber);
    User getUserByPhone(String phoneNumber);
    User saveUser(User user);
    User register(User user);
    User getUserById(Long id);
    User updateUser(User user);
    void updateUserStatus(Long id, Integer status);
    void updateCreditScore(Long id, Integer creditScore);
    UserProfile getUserProfile(Long userId);
    UserProfile updateUserProfile(UserProfile userProfile);
    void deleteByPhone(String phone);
}
