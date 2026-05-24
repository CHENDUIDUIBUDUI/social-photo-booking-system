package com.photo.booking.service.impl;

import com.photo.booking.entity.User;
import com.photo.booking.entity.UserProfile;
import com.photo.booking.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        // 准备测试数据
        String openid = "test-openid";
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setOpenid(openid);
        expectedUser.setNickname("test-user");

        // 模拟mapper行为
        when(userMapper.selectByOpenid(openid)).thenReturn(expectedUser);

        // 执行测试
        User actualUser = userService.login(openid);

        // 验证结果
        assertEquals(expectedUser, actualUser);
        verify(userMapper, times(1)).selectByOpenid(openid);
    }

    @Test
    void testRegister() {
        // 准备测试数据
        User user = new User();
        user.setOpenid("test-openid");
        user.setNickname("test-user");

        // 执行测试
        User actualUser = userService.register(user);

        // 验证结果
        assertEquals(user, actualUser);
        verify(userMapper, times(1)).insert(user);
    }

    @Test
    void testGetUserById() {
        // 准备测试数据
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setNickname("test-user");

        // 模拟mapper行为
        when(userMapper.selectById(userId)).thenReturn(expectedUser);

        // 执行测试
        User actualUser = userService.getUserById(userId);

        // 验证结果
        assertEquals(expectedUser, actualUser);
        verify(userMapper, times(1)).selectById(userId);
    }

    @Test
    void testUpdateUser() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setNickname("updated-user");

        // 执行测试
        User actualUser = userService.updateUser(user);

        // 验证结果
        assertEquals(user, actualUser);
        verify(userMapper, times(1)).update(user);
    }

    @Test
    void testUpdateUserStatus() {
        // 准备测试数据
        Long userId = 1L;
        Integer status = 0;

        // 执行测试
        userService.updateUserStatus(userId, status);

        // 验证结果
        verify(userMapper, times(1)).updateStatus(userId, status);
    }

    @Test
    void testUpdateCreditScore() {
        // 准备测试数据
        Long userId = 1L;
        Integer creditScore = 95;

        // 执行测试
        userService.updateCreditScore(userId, creditScore);

        // 验证结果
        verify(userMapper, times(1)).updateCreditScore(userId, creditScore);
    }

    @Test
    void testGetUserProfile() {
        // 准备测试数据
        Long userId = 1L;
        UserProfile expectedProfile = new UserProfile();
        expectedProfile.setUserId(userId);
        expectedProfile.setRealName("Test User");

        // 模拟mapper行为
        when(userMapper.selectUserProfileByUserId(userId)).thenReturn(expectedProfile);

        // 执行测试
        UserProfile actualProfile = userService.getUserProfile(userId);

        // 验证结果
        assertEquals(expectedProfile, actualProfile);
        verify(userMapper, times(1)).selectUserProfileByUserId(userId);
    }

    @Test
    void testUpdateUserProfile() {
        // 准备测试数据
        UserProfile profile = new UserProfile();
        profile.setUserId(1L);
        profile.setRealName("Updated Name");

        // 模拟mapper行为
        when(userMapper.selectUserProfileByUserId(profile.getUserId())).thenReturn(null);

        // 执行测试
        UserProfile actualProfile = userService.updateUserProfile(profile);

        // 验证结果
        assertEquals(profile, actualProfile);
        verify(userMapper, times(1)).insertUserProfile(profile);
    }

    @Test
    void testUpdateUserProfile_Existing() {
        // 准备测试数据
        UserProfile profile = new UserProfile();
        profile.setUserId(1L);
        profile.setRealName("Updated Name");

        // 模拟mapper行为
        UserProfile existingProfile = new UserProfile();
        existingProfile.setUserId(1L);
        existingProfile.setRealName("Original Name");
        when(userMapper.selectUserProfileByUserId(profile.getUserId())).thenReturn(existingProfile);

        // 执行测试
        UserProfile actualProfile = userService.updateUserProfile(profile);

        // 验证结果
        assertEquals(profile, actualProfile);
        verify(userMapper, times(1)).updateUserProfile(profile);
    }
}
