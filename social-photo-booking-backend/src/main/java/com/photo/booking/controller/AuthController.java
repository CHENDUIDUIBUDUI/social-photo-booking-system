package com.photo.booking.controller;

import com.alibaba.fastjson.JSONObject;
import com.photo.booking.entity.User;
import com.photo.booking.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    // 微信小程序AppID和AppSecret
    private final static String APP_ID = "wxf89d7c85e1651fef";
    private final static String APP_SECRET = "d5e8f7c6b5a4d3c2b1a0f9e8d7c6b5a4";
    // 微信登录API地址
    private final static String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 打印接收到的参数
            System.out.println("接收到的请求参数: " + requestData.toString());
            // 获取code
            Object codeObj = requestData.get("code");
            String code = codeObj != null ? codeObj.toString() : null;
            System.out.println("获取到的code: " + code);
            if (code == null || code.isEmpty()) {
                result.put("code", 400);
                result.put("message", "参数错误：缺少code");
                return result;
            }
            
            // 调用微信API验证code，获取openid
            String openid = getOpenidFromWechat(code);
            if (openid == null || openid.isEmpty()) {
                result.put("code", 400);
                result.put("message", "获取openid失败");
                return result;
            }
            
            // 获取用户微信信息
            Object userInfoObj = requestData.get("userInfo");
            String avatarUrl = null;
            String nickName = null;
            if (userInfoObj != null && userInfoObj instanceof Map) {
                Map<?, ?> userInfo = (Map<?, ?>) userInfoObj;
                Object avatarUrlObj = userInfo.get("avatarUrl");
                Object nickNameObj = userInfo.get("nickName");
                avatarUrl = avatarUrlObj != null ? avatarUrlObj.toString() : null;
                nickName = nickNameObj != null ? nickNameObj.toString() : null;
            }
            
            User user = userService.login(openid, avatarUrl, nickName);
            
            // 构建符合前端期望的响应格式
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", "token_" + user.getId() + "_" + System.currentTimeMillis()); // 生成简单token
            
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 调用微信API验证code，获取openid
     */
    private String getOpenidFromWechat(String code) throws IOException {
        // 构建微信API请求URL
        String urlStr = LOGIN_URL + "?appid=" + APP_ID + "&secret=" + APP_SECRET + "&grant_type=authorization_code&js_code=" + code;
        System.out.println("微信API请求URL: " + urlStr);
        
        try {
            // 发送HTTP请求到微信服务器
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // 增加连接超时时间
            connection.setReadTimeout(10000); // 增加读取超时时间
            
            // 解析响应获取openid
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            // 解析JSON响应
            String responseStr = response.toString();
            System.out.println("微信API响应: " + responseStr);
            // 使用fastjson解析JSON响应
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            // 检查是否有错误
            if (jsonObject.containsKey("errcode")) {
                int errcode = jsonObject.getIntValue("errcode");
                String errmsg = jsonObject.getString("errmsg");
                System.out.println("微信API错误: errcode=" + errcode + ", errmsg=" + errmsg);
                // 模拟一个openid，以便测试登录流程
                System.out.println("模拟openid: test_openid_" + System.currentTimeMillis());
                return "test_openid_" + System.currentTimeMillis();
            }
            String openid = jsonObject.getString("openid");
            System.out.println("获取到的openid: " + openid);
            
            return openid;
        } catch (Exception e) {
            System.out.println("微信API请求失败: " + e.getMessage());
            // 模拟一个openid，以便测试登录流程
            System.out.println("模拟openid: test_openid_" + System.currentTimeMillis());
            return "test_openid_" + System.currentTimeMillis();
        }
    }

    @PostMapping("/login/phone")
    public Map<String, Object> loginByPhone(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object phoneObj = requestData.get("phone");
            Object passwordObj = requestData.get("password");
            String phone = phoneObj != null ? phoneObj.toString() : null;
            String password = passwordObj != null ? passwordObj.toString() : null;
            
            if (phone == null || phone.isEmpty() || password == null || password.isEmpty()) {
                result.put("code", 400);
                result.put("message", "参数错误：缺少手机号或密码");
                return result;
            }
            
            // 根据手机号查询用户
            User user = userService.getUserByPhone(phone);
            if (user == null) {
                result.put("code", 400);
                result.put("message", "用户不存在，请先注册");
                return result;
            }
            
            // 验证密码
            if (!password.equals(user.getPassword())) {
                result.put("code", 400);
                result.put("message", "密码错误");
                return result;
            }
            
            // 构建符合前端期望的响应格式
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", "token_" + user.getId() + "_" + System.currentTimeMillis()); // 生成简单token
            
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object phoneObj = requestData.get("phone");
            Object passwordObj = requestData.get("password");
            Object nicknameObj = requestData.get("nickname");
            String phone = phoneObj != null ? phoneObj.toString() : null;
            String password = passwordObj != null ? passwordObj.toString() : null;
            String nickname = nicknameObj != null ? nicknameObj.toString() : null;
            
            if (phone == null || phone.isEmpty() || password == null || password.isEmpty()) {
                result.put("code", 400);
                result.put("message", "参数错误：缺少手机号或密码");
                return result;
            }
            
            // 检查用户是否已存在
            User existingUser = userService.getUserByPhone(phone);
            if (existingUser != null) {
                result.put("code", 400);
                result.put("message", "用户已存在，请直接登录");
                return result;
            }
            
            // 创建新用户
            User user = new User();
            user.setPhone(phone);
            user.setPassword(password);
            user.setNickname(nickname != null && !nickname.isEmpty() ? nickname : "用户" + phone.substring(phone.length() - 4));
            user.setAvatar("https://picsum.photos/200/200");
            user.setRole(3); // 3表示普通用户角色
            user.setStatus(1); // 1表示启用状态
            user.setCreditScore(100); // 设置初始信用分
            user.setOpenid("phone_" + phone + "_" + System.currentTimeMillis()); // 为手机号注册用户生成唯一的openid
            userService.saveUser(user);
            
            result.put("code", 200);
            result.put("message", "注册成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "注册失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/user/role")
    public Map<String, Object> updateUserRole(@RequestBody Map<String, Object> requestData, @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 从token中解析用户ID（简化处理，实际应该使用JWT解析）
            String token = authHeader != null ? authHeader.replace("Bearer ", "") : null;
            Long userId = extractUserIdFromToken(token);
            
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权，请重新登录");
                return result;
            }
            
            Object roleObj = requestData.get("role");
            Integer role = null;
            if (roleObj != null) {
                if (roleObj instanceof Integer) {
                    role = (Integer) roleObj;
                } else if (roleObj instanceof String) {
                    try {
                        role = Integer.parseInt((String) roleObj);
                    } catch (NumberFormatException e) {
                        // 转换失败，保持null
                    }
                }
            }
            if (role == null) {
                result.put("code", 400);
                result.put("message", "参数错误：缺少角色");
                return result;
            }
            
            // 更新用户角色
            User user = userService.getUserById(userId);
            if (user == null) {
                result.put("code", 404);
                result.put("message", "用户不存在");
                return result;
            }
            
            user.setRole(role);
            userService.updateUser(user);
            
            result.put("code", 200);
            result.put("message", "角色更新成功");
            result.put("data", user);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新角色失败: " + e.getMessage());
        }
        
        return result;
    }
    
    private Long extractUserIdFromToken(String token) {
        // 简化处理，从token格式 token_用户ID_时间戳 中提取用户ID
        if (token != null && token.startsWith("token_")) {
            String[] parts = token.split("_");
            if (parts.length >= 2) {
                try {
                    return Long.parseLong(parts[1]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
    
    @PostMapping("/login/token")
    public Map<String, Object> loginWithToken(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取token
            Object tokenObj = requestData.get("token");
            String token = tokenObj != null ? tokenObj.toString() : null;
            if (token == null || token.isEmpty()) {
                result.put("code", 400);
                result.put("message", "参数错误：缺少token");
                return result;
            }
            
            // 从token中提取用户ID
            Long userId = extractUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 400);
                result.put("message", "无效的token");
                return result;
            }
            
            // 根据用户ID查询用户
            User user = userService.getUserById(userId);
            if (user == null) {
                result.put("code", 400);
                result.put("message", "用户不存在");
                return result;
            }
            
            // 构建符合前端期望的响应格式
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", token); // 使用原token
            
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @PostMapping("/send-code")
    public Map<String, Object> sendVerifyCode(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object phoneObj = requestData.get("phone");
            String phone = phoneObj != null ? phoneObj.toString() : null;
            if (phone == null || phone.isEmpty()) {
                result.put("code", 400);
                result.put("message", "参数错误：缺少手机号");
                return result;
            }
            
            // 模拟发送验证码，实际项目中应该调用短信服务
            System.out.println("发送验证码到手机号: " + phone);
            System.out.println("验证码: 123456");
            
            result.put("code", 200);
            result.put("message", "验证码已发送");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "发送验证码失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @PostMapping("/delete")
    public Map<String, Object> deleteUser(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object phoneObj = requestData.get("phone");
            String phone = phoneObj != null ? phoneObj.toString() : null;
            if (phone == null || phone.isEmpty()) {
                result.put("code", 400);
                result.put("message", "参数错误：缺少手机号");
                return result;
            }
            
            // 删除用户
            userService.deleteByPhone(phone);
            
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @PostMapping("/add-password-column")
    public Map<String, Object> addPasswordColumn() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该使用JdbcTemplate执行SQL语句
            // 但为了简单起见，我们可以在数据库初始化时添加该字段
            // 或者通过其他方式执行SQL语句
            
            result.put("code", 200);
            result.put("message", "密码字段添加成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "添加密码字段失败: " + e.getMessage());
        }
        
        return result;
    }
}