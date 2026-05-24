package com.photo.booking.controller;

import com.photo.booking.service.ContentService;
import com.photo.booking.entity.Content;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Resource
    private ContentService contentService;

    @GetMapping("/list")
    public Map<String, Object> getBannerList() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取所有作品并按点赞数和评论数排序
            List<Content> allContents = contentService.getAllContents();
            allContents.sort((c1, c2) -> {
                int score1 = c1.getLikeCount() * 2 + c1.getCommentCount();
                int score2 = c2.getLikeCount() * 2 + c2.getCommentCount();
                return score2 - score1; // 降序排序
            });
            
            // 去重，确保每个摄影师只出现一次，且图片URL不重复
            Set<String> imageUrls = new HashSet<>();
            List<Map<String, Object>> bannerList = new ArrayList<>();
            
            // 首先从所有作品中选择不同图片
            for (Content content : allContents) {
                if (content.getCoverImage() != null && !content.getCoverImage().isEmpty() && 
                    !imageUrls.contains(content.getCoverImage()) && bannerList.size() < 10) {
                    imageUrls.add(content.getCoverImage());
                    Map<String, Object> banner = new HashMap<>();
                    banner.put("id", content.getId());
                    banner.put("imageUrl", content.getCoverImage());
                    banner.put("title", content.getTitle());
                    bannerList.add(banner);
                }
            }
            
            // 如果不足5个，从热门作品中补充（考虑去重）
            if (bannerList.size() < 5) {
                List<Content> hotContents = contentService.getHotContents(30);
                for (Content content : hotContents) {
                    if (content.getCoverImage() != null && !content.getCoverImage().isEmpty() && 
                        bannerList.size() < 10 && !imageUrls.contains(content.getCoverImage())) {
                        imageUrls.add(content.getCoverImage());
                        Map<String, Object> banner = new HashMap<>();
                        banner.put("id", content.getId());
                        banner.put("imageUrl", content.getCoverImage());
                        banner.put("title", content.getTitle());
                        bannerList.add(banner);
                    }
                }
            }
            
            // 如果仍然不足5个，使用默认轮播图
            if (bannerList.size() < 5) {
                String[] defaultImages = {
                    "https://picsum.photos/750/300?random=1",
                    "https://picsum.photos/750/300?random=2",
                    "https://picsum.photos/750/300?random=3",
                    "https://picsum.photos/750/300?random=4",
                    "https://picsum.photos/750/300?random=5"
                };
                String[] defaultTitles = {
                    "专业摄影服务",
                    "精彩瞬间捕捉",
                    "艺术摄影创作",
                    "创意视觉体验",
                    "专业影像记录"
                };
                
                for (int i = bannerList.size(); i < 5; i++) {
                    Map<String, Object> banner = new HashMap<>();
                    banner.put("id", (long) (i + 1000));
                    banner.put("imageUrl", defaultImages[i]);
                    banner.put("title", defaultTitles[i]);
                    bannerList.add(banner);
                }
            }
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", bannerList);
        } catch (Exception e) {
            // 出错时返回默认轮播图
            List<Map<String, Object>> defaultBannerList = new ArrayList<>();
            String[] defaultImages = {
                "https://picsum.photos/750/300?random=1",
                "https://picsum.photos/750/300?random=2",
                "https://picsum.photos/750/300?random=3"
            };
            String[] defaultTitles = {
                "专业摄影服务",
                "精彩瞬间捕捉",
                "艺术摄影创作"
            };
            
            for (int i = 0; i < 3; i++) {
                Map<String, Object> banner = new HashMap<>();
                banner.put("id", (long) (i + 1000));
                banner.put("imageUrl", defaultImages[i]);
                banner.put("title", defaultTitles[i]);
                defaultBannerList.add(banner);
            }
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", defaultBannerList);
        }
        return result;
    }
}