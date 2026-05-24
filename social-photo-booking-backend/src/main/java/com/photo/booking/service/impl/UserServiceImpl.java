package com.photo.booking.service.impl;

import com.photo.booking.entity.User;
import com.photo.booking.entity.UserProfile;
import com.photo.booking.entity.Photographer;
import com.photo.booking.mapper.UserMapper;
import com.photo.booking.mapper.PhotographerMapper;
import com.photo.booking.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    
    @Resource
    private PhotographerMapper photographerMapper;

    @Override
    public User login(String openid) {
        return login(openid, null, null);
    }

    @Override
    public User login(String openid, String avatarUrl, String nickName) {
        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            // 用户不存在，自动注册
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickName != null ? nickName : "微信用户_" + openid.substring(openid.length() - 8));
            user.setAvatar(avatarUrl);
            user.setRole(3); // 默认普通用户
            user.setStatus(1);
            user.setCreditScore(100);
            userMapper.insert(user);
            // 重新查询获取完整信息
            user = userMapper.selectByOpenid(openid);
        } else {
            // 用户存在，更新头像和昵称
            boolean updated = false;
            if (nickName != null && !nickName.equals(user.getNickname())) {
                user.setNickname(nickName);
                updated = true;
            }
            if (avatarUrl != null && !avatarUrl.equals(user.getAvatar())) {
                user.setAvatar(avatarUrl);
                updated = true;
            }
            if (updated) {
                userMapper.update(user);
            }
        }
        return user;
    }

    @Override
    public User loginByPhone(String phoneNumber) {
        User user = userMapper.selectByPhone(phoneNumber);
        if (user == null) {
            // 用户不存在，自动注册
            user = new User();
            user.setPhone(phoneNumber);
            user.setNickname("手机用户");
            user.setRole(3); // 默认普通用户
            user.setStatus(1);
            user.setCreditScore(100);
            userMapper.insert(user);
            // 重新查询获取完整信息
            user = userMapper.selectByPhone(phoneNumber);
        }
        return user;
    }

    @Override
    public User getUserByPhone(String phoneNumber) {
        return userMapper.selectByPhone(phoneNumber);
    }

    @Override
    public User saveUser(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public User register(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User updateUser(User user) {
        // 获取原始用户信息
        User originalUser = userMapper.selectById(user.getId());
        // 检查角色是否从非摄影师变为摄影师
        if (originalUser != null && originalUser.getRole() != 1 && user.getRole() == 1) {
            // 检查是否已经存在摄影师记录
            Photographer existingPhotographer = photographerMapper.selectByUserId(user.getId());
            if (existingPhotographer == null) {
                // 创建摄影师记录
                try {
                    Photographer photographer = new Photographer();
                    photographer.setUserId(user.getId());
                    photographer.setName(user.getNickname());
                    photographer.setAvatar(user.getAvatar());
                    photographer.setCertified(0);
                    photographer.setOrders(0);
                    photographer.setRating(BigDecimal.ZERO);
                    photographer.setPrice(BigDecimal.ZERO);
                    photographer.setStatus(1);
                    // 插入摄影师记录
                    photographerMapper.insert(photographer);
                } catch (Exception e) {
                    // 记录错误但不影响用户更新
                    e.printStackTrace();
                }
            }
        } else if (originalUser != null && originalUser.getRole() == 1 && user.getRole() != 1) {
            // 检查是否存在摄影师记录
            Photographer existingPhotographer = photographerMapper.selectByUserId(user.getId());
            if (existingPhotographer != null) {
                // 这里可以添加删除摄影师记录的逻辑
                // 但为了数据安全，建议先软删除或保留记录
                // 暂时只更新状态
                try {
                    existingPhotographer.setStatus(0); // 禁用摄影师记录
                    photographerMapper.update(existingPhotographer);
                } catch (Exception e) {
                    // 记录错误但不影响用户更新
                    e.printStackTrace();
                }
            }
        }
        userMapper.update(user);
        return user;
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        userMapper.updateStatus(id, status);
    }

    @Override
    public void updateCreditScore(Long id, Integer creditScore) {
        userMapper.updateCreditScore(id, creditScore);
    }

    @Override
    public UserProfile getUserProfile(Long userId) {
        return userMapper.selectUserProfileByUserId(userId);
    }

    @Override
    public UserProfile updateUserProfile(UserProfile userProfile) {
        UserProfile existingProfile = userMapper.selectUserProfileByUserId(userProfile.getUserId());
        if (existingProfile == null) {
            userMapper.insertUserProfile(userProfile);
        } else {
            userMapper.updateUserProfile(userProfile);
        }
        return userProfile;
    }

    @Override
    public void deleteByPhone(String phone) {
        userMapper.deleteByPhone(phone);
    }
}
