package com.photo.booking.controller;

import com.photo.booking.service.LikeService;
import com.photo.booking.service.CommentLikeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Resource
    private LikeService likeService;

    @Resource
    private CommentLikeService commentLikeService;

    @PostMapping("/content")
    public Map<String, Object> likeContent(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = Long.parseLong(params.get("userId").toString());
            Long contentId = Long.parseLong(params.get("contentId").toString());
            boolean liked = Boolean.parseBoolean(params.get("liked").toString());
            
            boolean success;
            if (liked) {
                success = likeService.addLike(userId, contentId);
            } else {
                success = likeService.removeLike(userId, contentId);
            }
            if (success) {
                int likeCount = likeService.getLikeCount(contentId);
                result.put("code", 200);
                result.put("message", "操作成功");
                result.put("data", Map.of("likeCount", likeCount, "liked", liked));
            } else {
                result.put("code", 400);
                result.put("message", "操作失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/comment")
    public Map<String, Object> likeComment(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = Long.parseLong(params.get("userId").toString());
            Long commentId = Long.parseLong(params.get("commentId").toString());
            boolean liked = Boolean.parseBoolean(params.get("liked").toString());
            
            boolean success;
            if (liked) {
                success = commentLikeService.addCommentLike(userId, commentId);
            } else {
                success = commentLikeService.removeCommentLike(userId, commentId);
            }
            if (success) {
                int likeCount = commentLikeService.getCommentLikeCount(commentId);
                result.put("code", 200);
                result.put("message", "操作成功");
                result.put("data", Map.of("likeCount", likeCount, "liked", liked));
            } else {
                result.put("code", 400);
                result.put("message", "操作失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/content/status")
    public Map<String, Object> getContentLikeStatus(@RequestParam(required = false) Long userId, @RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean liked = false;
            if (userId != null) {
                liked = likeService.isLiked(userId, contentId);
            }
            int likeCount = likeService.getLikeCount(contentId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", Map.of("liked", liked, "likeCount", likeCount));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/comment/status")
    public Map<String, Object> getCommentLikeStatus(@RequestParam Long userId, @RequestParam Long commentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean liked = commentLikeService.isCommentLiked(userId, commentId);
            int likeCount = commentLikeService.getCommentLikeCount(commentId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", Map.of("liked", liked, "likeCount", likeCount));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
        }
        return result;
    }
}
