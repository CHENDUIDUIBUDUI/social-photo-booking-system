package com.socialshoot.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.socialshoot.admin.entity.User;
import com.socialshoot.admin.mapper.UserMapper;
import com.socialshoot.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<User> getPageList(int pageNum, int pageSize, String nickname, String phone, String role) {
        PageHelper.startPage(pageNum, pageSize);
        Integer roleId = null;
        if (role != null && !role.isEmpty()) {
            if ("photographer".equals(role)) {
                roleId = 1;
            } else if ("model".equals(role)) {
                roleId = 2;
            } else if ("user".equals(role)) {
                roleId = 3;
            }
        }
        List<User> userList = userMapper.selectByCondition(nickname, phone, roleId);
        return new PageInfo<>(userList);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getByOpenId(String openId) {
        return userMapper.selectByOpenId(openId);
    }

    @Override
    public User getByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public void save(User user) {
        if (user.getId() != null) {
            userMapper.update(user);
        } else {
            userMapper.insert(user);
        }
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return userMapper.updateStatus(id, status) > 0;
    }

    @Override
    public boolean bindPhone(Long userId, String phone) {
        return userMapper.updatePhone(userId, phone) > 0;
    }

    @Override
    public boolean updateRole(Long id, Integer roleId) {
        return userMapper.updateRoleId(id, roleId) > 0;
    }

    @Override
    public List<User> getHotPhotographers(int limit) {
        return userMapper.selectHotPhotographers(limit);
    }

    @Override
    public List<User> getPhotographerList(String region, String style, Integer maxPrice) {
        return userMapper.selectAll();
    }
    
    @Override
    public List<User> search(String keyword, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return userMapper.search(keyword);
    }
}
