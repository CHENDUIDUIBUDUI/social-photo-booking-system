package com.photo.booking.controller;

import com.photo.booking.entity.Comment;
import com.photo.booking.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public Map<String, Object> addComment(@RequestParam Long userId, @RequestParam Long contentId, @RequestParam String content) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = commentService.addComment(userId, contentId, content);
            if (success) {
                int commentCount = commentService.getCommentCount(contentId);
                result.put("code", 200);
                result.put("message", "评论成功");
                result.put("data", Map.of("commentCount", commentCount));
            } else {
                result.put("code", 400);
                result.put("message", "评论失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/delete")
    public Map<String, Object> deleteComment(@RequestParam Long userId, @RequestParam Long commentId, @RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = commentService.deleteComment(userId, commentId);
            if (success) {
                int commentCount = commentService.getCommentCount(contentId);
                result.put("code", 200);
                result.put("message", "删除评论成功");
                result.put("data", Map.of("commentCount", commentCount));
            } else {
                result.put("code", 400);
                result.put("message", "删除评论失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> getComments(@RequestParam Long contentId, @RequestParam(required = false) Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Comment> comments = commentService.getCommentsByContentId(contentId, userId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", comments);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count")
    public Map<String, Object> getCommentCount(@RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = commentService.getCommentCount(contentId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", Map.of("count", count));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
}
