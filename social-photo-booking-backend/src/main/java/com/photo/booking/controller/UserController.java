package com.photo.booking.controller;

import com.photo.booking.entity.User;
import com.photo.booking.entity.UserProfile;
import com.photo.booking.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public User login(@RequestParam String openid) {
        return userService.login(openid);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/info")
    public Map<String, Object> getUserInfo(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                result.put("code", 200);
                result.put("message", "success");
                result.put("data", user);
            } else {
                result.put("code", 404);
                result.put("message", "用户不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/status")
    public void updateUserStatus(@RequestParam Long id, @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
    }

    @PutMapping("/credit")
    public void updateCreditScore(@RequestParam Long id, @RequestParam Integer creditScore) {
        userService.updateCreditScore(id, creditScore);
    }

    @GetMapping("/profile")
    public UserProfile getUserProfile(@RequestParam Long userId) {
        return userService.getUserProfile(userId);
    }

    @PutMapping("/profile")
    public UserProfile updateUserProfile(@RequestBody UserProfile userProfile) {
        return userService.updateUserProfile(userProfile);
    }
}
