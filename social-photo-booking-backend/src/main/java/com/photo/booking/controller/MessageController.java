package com.photo.booking.controller;

import com.photo.booking.entity.Message;
import com.photo.booking.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message) {
        return messageService.sendMessage(message);
    }

    @PutMapping("/status")
    public void updateMessageStatus(@RequestParam Long id, @RequestParam Integer status) {
        messageService.updateMessageStatus(id, status);
    }

    @PutMapping("/status/user")
    public void updateMessageStatusByUserId(@RequestParam Long userId, @RequestParam Integer status) {
        messageService.updateMessageStatusByUserId(userId, status);
    }

    @GetMapping("/list")
    public List<Message> getMessageByUserId(@RequestParam Long userId, @RequestParam(required = false) Integer type) {
        return messageService.getMessageByUserId(userId, type);
    }

    @GetMapping("/chat")
    public List<Message> getChatMessages(@RequestParam Long userId1, @RequestParam Long userId2) {
        return messageService.getChatMessages(userId1, userId2);
    }

    @GetMapping("/unread/count")
    public int countUnreadMessage(@RequestParam Long userId) {
        return messageService.countUnreadMessage(userId);
    }
}
