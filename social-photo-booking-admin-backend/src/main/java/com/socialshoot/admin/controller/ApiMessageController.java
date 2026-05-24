package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Message;
import com.socialshoot.admin.mapper.MessageMapper;
import com.socialshoot.admin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
public class ApiMessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private MessageMapper messageMapper;
    
    @GetMapping("/list")
    public Map<String, Object> getMessageList(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            List<Message> messageList = messageService.findByUserId(userId, page, pageSize);
            int total = messageService.countByUserId(userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", messageList);
            data.put("total", total);
            data.put("page", page);
            data.put("pageSize", pageSize);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/chat")
    public Map<String, Object> getChatMessages(
            @RequestHeader("Authorization") String token,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long currentUserId = getUserIdFromToken(token);
            if (currentUserId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            List<Message> messageList = messageService.findBySenderIdAndReceiverId(currentUserId, userId, page, pageSize);
            int total = messageService.countBySenderIdAndReceiverId(currentUserId, userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", messageList);
            data.put("total", total);
            data.put("page", page);
            data.put("pageSize", pageSize);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/send")
    public Map<String, Object> sendMessage(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long senderId = getUserIdFromToken(token);
            if (senderId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            Long receiverId = Long.parseLong(params.get("receiverId").toString());
            String content = (String) params.get("content");
            
            if (receiverId == null || content == null || content.isEmpty()) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            Message message = new Message();
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);
            message.setContent(content);
            message.setIsRead(0);
            message.setCreateTime(LocalDateTime.now());
            
            messageMapper.insert(message);
            
            result.put("code", 200);
            result.put("data", message);
            result.put("message", "发送成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "发送失败：" + e.getMessage());
        }
        return result;
    }

    // 更新消息已读状态
    @PostMapping("/read")
    public Map<String, Object> markAsRead(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            Long messageId = Long.parseLong(params.get("messageId").toString());
            
            // 更新消息已读状态
            messageMapper.updateReadStatus(messageId, 1);
            
            result.put("code", 200);
            result.put("message", "标记已读成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }

    // 获取未读消息数量
    @GetMapping("/unread/count")
    public Map<String, Object> getUnreadCount(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            int unreadCount = messageService.countUnreadByUserId(userId);
            
            result.put("code", 200);
            result.put("data", unreadCount);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 发送系统通知
    @PostMapping("/system/send")
    public Map<String, Object> sendSystemMessage(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long receiverId = Long.parseLong(params.get("receiverId").toString());
            String content = (String) params.get("content");
            
            if (receiverId == null || content == null || content.isEmpty()) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            Message message = new Message();
            message.setSenderId(0L); // 0表示系统发送
            message.setReceiverId(receiverId);
            message.setContent(content);
            message.setIsRead(0);
            message.setCreateTime(LocalDateTime.now());
            
            messageMapper.insert(message);
            
            result.put("code", 200);
            result.put("data", message);
            result.put("message", "发送成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "发送失败：" + e.getMessage());
        }
        return result;
    }
    
    private Long getUserIdFromToken(String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            String[] parts = tokenValue.split("\\.");
            if (parts.length == 3) {
                String payload = parts[1];
                String decoded = new String(java.util.Base64.getUrlDecoder().decode(payload));
                return Long.parseLong(decoded.substring(decoded.indexOf("\"sub\":\"") + 7, decoded.indexOf("\",\"")));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}