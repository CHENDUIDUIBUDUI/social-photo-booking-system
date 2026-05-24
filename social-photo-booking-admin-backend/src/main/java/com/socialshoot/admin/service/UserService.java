package com.socialshoot.admin.service;

import com.github.pagehelper.PageInfo;
import com.socialshoot.admin.entity.User;
import java.util.List;

public interface UserService {
    PageInfo<User> getPageList(int pageNum, int pageSize, String nickname, String phone, String role);
    User getById(Long id);
    User getByOpenId(String openId);
    User getByPhone(String phone);
    void save(User user);
    boolean updateStatus(Long id, Integer status);
    boolean bindPhone(Long userId, String phone);
    boolean updateRole(Long id, Integer roleId);
    List<User> getHotPhotographers(int limit);
    List<User> getPhotographerList(String region, String style, Integer maxPrice);
    List<User> search(String keyword, int page, int pageSize);
}
