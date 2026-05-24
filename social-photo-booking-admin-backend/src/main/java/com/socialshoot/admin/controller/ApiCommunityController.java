package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Comment;
import com.socialshoot.admin.service.CommentService;
import com.socialshoot.admin.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
public class ApiCommunityController {
    
    @Autowired
    private LikeService likeService;
    
    @Autowired
    private CommentService commentService;
    
    // 点赞/取消点赞
    @PostMapping("/like")
    public Map<String, Object> toggleLike(
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
            
            Long contentId = Long.parseLong(params.get("contentId").toString());
            Integer type = Integer.valueOf(params.get("type").toString()); // 1: 点赞, 2: 收藏
            
            if (contentId == null || type == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            // 检查是否已点赞
            boolean isLiked = likeService.isLiked(userId, contentId, type);
            if (isLiked) {
                likeService.removeLike(userId, contentId, type);
                result.put("code", 200);
                result.put("message", "取消成功");
                result.put("isLiked", false);
            } else {
                likeService.addLike(userId, contentId, type);
                result.put("code", 200);
                result.put("message", "点赞成功");
                result.put("isLiked", true);
            }
            
            // 获取最新点赞数
            int likeCount = likeService.getLikeCount(contentId, type);
            result.put("likeCount", likeCount);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }
    
    // 获取点赞状态和数量
    @GetMapping("/like/status")
    public Map<String, Object> getLikeStatus(
            @RequestHeader("Authorization") String token,
            @RequestParam Long contentId,
            @RequestParam Integer type) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            if (contentId == null || type == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            boolean isLiked = likeService.isLiked(userId, contentId, type);
            int likeCount = likeService.getLikeCount(contentId, type);
            
            Map<String, Object> data = new HashMap<>();
            data.put("isLiked", isLiked);
            data.put("likeCount", likeCount);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 发表评论
    @PostMapping("/comment")
    public Map<String, Object> addComment(
            @RequestHeader("Authorization") String token,
            @RequestBody Comment comment) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "未授权");
                return result;
            }
            
            if (comment.getContentId() == null || comment.getContent() == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            comment.setUserId(userId);
            commentService.addComment(comment);
            
            result.put("code", 200);
            result.put("message", "评论成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "评论失败：" + e.getMessage());
        }
        return result;
    }
    
    // 删除评论
    @PostMapping("/comment/delete")
    public Map<String, Object> deleteComment(
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
            
            Long commentId = Long.parseLong(params.get("id").toString());
            if (commentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            commentService.deleteComment(commentId);
            
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    // 点赞评论
    @PostMapping("/comment/like")
    public Map<String, Object> likeComment(
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
            
            Long commentId = Long.parseLong(params.get("id").toString());
            if (commentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            commentService.likeComment(commentId);
            
            result.put("code", 200);
            result.put("message", "点赞成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "点赞失败：" + e.getMessage());
        }
        return result;
    }
    
    // 获取评论列表
    @GetMapping("/comments")
    public Map<String, Object> getComments(@RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (contentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            result.put("code", 200);
            result.put("data", commentService.getCommentsByContentId(contentId));
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 获取评论列表（支持分页和排序）- 适配小程序调用
    @GetMapping("/comment/list")
    public Map<String, Object> getCommentList(
            @RequestParam Long contentId,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "time") String sort) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (contentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            List<Comment> comments = commentService.getCommentsByContentId(contentId);
            
            // 模拟分页
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, comments.size());
            
            // 模拟排序
            if ("time".equals(sort)) {
                // 按时间排序（默认）
            } else if ("like".equals(sort)) {
                // 按点赞数排序（Comment实体没有likeCount字段，跳过排序）
            }
            
            List<Comment> pageComments = start < comments.size() ? comments.subList(start, end) : new java.util.ArrayList<>();
            
            result.put("code", 200);
            result.put("data", pageComments);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 获取点赞状态（适配小程序调用 /api/like/content/status）
    @GetMapping("/like/content/status")
    public Map<String, Object> getContentLikeStatus(
            @RequestParam Long userId,
            @RequestParam Long contentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (userId == null || contentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            boolean isLiked = likeService.isLiked(userId, contentId, 1); // type=1 表示点赞
            int likeCount = likeService.getLikeCount(contentId, 1);
            
            Map<String, Object> data = new HashMap<>();
            data.put("isLiked", isLiked);
            data.put("likeCount", likeCount);
            
            result.put("code", 200);
            result.put("data", data);
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 获取回复列表
    @GetMapping("/comments/replies")
    public Map<String, Object> getReplies(@RequestParam Long parentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (parentId == null) {
                result.put("code", 400);
                result.put("message", "参数错误");
                return result;
            }
            
            result.put("code", 200);
            result.put("data", commentService.getRepliesByParentId(parentId));
            result.put("message", "获取成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
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