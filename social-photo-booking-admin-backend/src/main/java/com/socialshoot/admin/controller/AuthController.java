package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.User;
import com.socialshoot.admin.service.UserService;
import com.socialshoot.admin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private static Map<String, String> verificationCodes = new HashMap<>();

    private String getRoleString(Integer roleId) {
        if (roleId == null) return "user";
        switch (roleId) {
            case 1: return "photographer";
            case 2: return "model";
            default: return "user";
        }
    }

    @PostMapping("/login")
    public Map<String, Object> wechatLogin(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        System.out.println("收到登录请求，参数: " + params);
        try {
            Object codeObj = params.get("code");
            String code = codeObj != null ? codeObj.toString() : null;
            Object userInfoObj = params.get("userInfo");
            Map<?, ?> userInfoMap = null;
            if (userInfoObj != null && userInfoObj instanceof Map) {
                userInfoMap = (Map<?, ?>) userInfoObj;
            }

            Object openIdObj = params.get("openId");
            if (openIdObj == null) {
                openIdObj = params.get("openid");
            }
            String openId = openIdObj != null ? openIdObj.toString() : null;
            if (openId == null || openId.isEmpty()) {
                openId = "test_openid_" + System.currentTimeMillis();
            }
            String unionId = "test_unionid_" + System.currentTimeMillis();

            User user = userService.getByOpenId(openId);

            if (user == null) {
                user = new User();
                user.setOpenId(openId);
                user.setUnionId(unionId);
                if (userInfoMap != null) {
                    Object nickNameObj = userInfoMap.get("nickName");
                    Object avatarUrlObj = userInfoMap.get("avatarUrl");
                    user.setNickname(nickNameObj != null ? nickNameObj.toString() : null);
                    user.setAvatar(avatarUrlObj != null ? avatarUrlObj.toString() : null);
                }
                user.setRoleId(3);
                user.setCreditScore(100);
                user.setStatus(1);
                user.setLastLoginTime(LocalDateTime.now());
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                userService.save(user);
            } else {
                if (userInfoMap != null) {
                    Object nickNameObj = userInfoMap.get("nickName");
                    Object avatarUrlObj = userInfoMap.get("avatarUrl");
                    if (nickNameObj != null) {
                        user.setNickname(nickNameObj.toString());
                    }
                    if (avatarUrlObj != null) {
                        user.setAvatar(avatarUrlObj.toString());
                    }
                }
                user.setLastLoginTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                userService.save(user);
            }

            String token = jwtUtil.generateToken(user.getId(), user.getNickname(), getRoleString(user.getRoleId()));

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);

            result.put("code", 200);
            result.put("data", data);
            result.put("message", "登录成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/send-code")
    public Map<String, Object> sendVerifyCode(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            String phone = params.get("phone");
            if (phone == null || phone.isEmpty()) {
                result.put("code", 400);
                result.put("message", "手机号不能为空");
                return result;
            }

            String code = String.format("%06d", new Random().nextInt(999999));
            verificationCodes.put(phone, code);

            System.out.println("向手机号 " + phone + " 发送验证码：" + code);

            result.put("code", 200);
            result.put("message", "验证码已发送");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "发送验证码失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/login/phone")
    public Map<String, Object> phoneLogin(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            String phone = params.get("phone") != null ? params.get("phone").toString() : null;
            String code = params.get("code") != null ? params.get("code").toString() : null;
            String password = params.get("password") != null ? params.get("password").toString() : null;

            if (phone == null || phone.isEmpty()) {
                result.put("code", 400);
                result.put("message", "手机号不能为空");
                return result;
            }

            if (code != null && !code.isEmpty()) {
                String storedCode = verificationCodes.get(phone);
                if (storedCode == null || !storedCode.equals(code)) {
                    result.put("code", 401);
                    result.put("message", "验证码错误");
                    return result;
                }
            } else if (password != null && !password.isEmpty()) {
            } else {
                result.put("code", 400);
                result.put("message", "验证码或密码不能为空");
                return result;
            }

            User user = userService.getByPhone(phone);

            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setNickname("用户" + phone.substring(7));
                user.setAvatar("https://via.placeholder.com/150");
                user.setRoleId(3);
                user.setCreditScore(100);
                user.setStatus(1);
                user.setLastLoginTime(LocalDateTime.now());
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                userService.save(user);
            } else {
                user.setLastLoginTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                userService.save(user);
            }

            String token = jwtUtil.generateToken(user.getId(), user.getNickname(), getRoleString(user.getRoleId()));

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);

            result.put("code", 200);
            result.put("data", data);
            result.put("message", "登录成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/user/role")
    public Map<String, Object> updateUserRole(@RequestBody Map<String, Object> params, @RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            Object roleObj = params.get("role");
            Integer role = null;
            if (roleObj != null) {
                if (roleObj instanceof String) {
                    try {
                        role = Integer.parseInt((String) roleObj);
                    } catch (NumberFormatException e) {
                    }
                } else if (roleObj instanceof Integer) {
                    role = (Integer) roleObj;
                } else if (roleObj instanceof Number) {
                    role = ((Number) roleObj).intValue();
                }
            }
            if (role == null) {
                result.put("code", 400);
                result.put("message", "角色不能为空");
                return result;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("role", role);

            result.put("code", 200);
            result.put("data", data);
            result.put("message", "角色更新成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "角色更新失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/bind-phone")
    public Map<String, Object> bindPhone(@RequestBody Map<String, Object> params, @RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            Object phoneObj = params.get("phone");
            Object codeObj = params.get("code");
            Object userIdStrObj = params.get("userId");
            String phone = phoneObj != null ? phoneObj.toString() : null;
            String code = codeObj != null ? codeObj.toString() : null;
            String userIdStr = userIdStrObj != null ? userIdStrObj.toString() : null;

            if (phone == null || phone.isEmpty() || code == null || code.isEmpty() || userIdStr == null) {
                result.put("code", 400);
                result.put("message", "手机号、验证码和用户ID不能为空");
                return result;
            }

            String storedCode = verificationCodes.get(phone);
            if (storedCode == null || !storedCode.equals(code)) {
                result.put("code", 401);
                result.put("message", "验证码错误");
                return result;
            }

            User existingUser = userService.getByPhone(phone);
            if (existingUser != null && !existingUser.getId().toString().equals(userIdStr)) {
                result.put("code", 400);
                result.put("message", "该手机号已被其他用户绑定");
                return result;
            }

            Long userId = Long.parseLong(userIdStr);
            boolean success = userService.bindPhone(userId, phone);

            if (success) {
                result.put("code", 200);
                result.put("message", "手机号绑定成功");
            } else {
                result.put("code", 400);
                result.put("message", "绑定失败，请重试");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "绑定失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/login/token")
    public Map<String, Object> loginWithToken(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Object tokenObj = params.get("token");
            String token = tokenObj != null ? tokenObj.toString() : null;

            if (token == null || token.isEmpty()) {
                result.put("code", 400);
                result.put("message", "token不能为空");
                return result;
            }

            Map<String, Object> claims = jwtUtil.parseToken(token);
            if (claims == null) {
                result.put("code", 401);
                result.put("message", "token无效");
                return result;
            }

            Object userIdObj = claims.get("userId");
            if (userIdObj == null) {
                result.put("code", 401);
                result.put("message", "token无效");
                return result;
            }

            Long userId = Long.parseLong(userIdObj.toString());
            User user = userService.getById(userId);

            if (user == null) {
                result.put("code", 401);
                result.put("message", "用户不存在");
                return result;
            }

            String newToken = jwtUtil.generateToken(user.getId(), user.getNickname(), getRoleString(user.getRoleId()));

            Map<String, Object> data = new HashMap<>();
            data.put("token", newToken);
            data.put("user", user);

            result.put("code", 200);
            result.put("data", data);
            result.put("message", "登录成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败：" + e.getMessage());
        }
        return result;
    }
}
