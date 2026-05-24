package com.socialshoot.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.socialshoot.admin.entity.Content;
import com.socialshoot.admin.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class ApiContentController {

    @Autowired
    private ContentService contentService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 创建内容（作品或需求）
    @PostMapping("/create")
    public Map<String, Object> createContent(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Content content = new Content();
            content.setUserId(Long.valueOf(params.get("userId").toString()));
            content.setTitle((String) params.get("title"));
            content.setDescription((String) params.get("description"));
            
            String coverImage = (String) params.get("coverImage");
            if (coverImage != null && !coverImage.isEmpty()) {
                content.setCoverImage(coverImage);
            }
            
            content.setLocation((String) params.get("location"));
            content.setPrice(params.get("price") != null ? Double.valueOf(params.get("price").toString()) : 0.0);
            content.setType(Integer.valueOf(params.get("type").toString()));
            content.setStatus(0); // 待审核
            content.setLikes(0);
            content.setComments(0);
            content.setViews(0);
            content.setCreateTime(LocalDateTime.now());
            content.setUpdateTime(LocalDateTime.now());

            contentService.save(content);

            Object tagsObj = params.get("tags");
            if (tagsObj != null) {
                List<String> tagNames;
                if (tagsObj instanceof List) {
                    tagNames = (List<String>) tagsObj;
                } else {
                    tagNames = objectMapper.readValue(tagsObj.toString(), 
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                }
                saveContentTags(content.getId(), tagNames);
            }

            result.put("code", 200);
            result.put("message", "发布成功");
            result.put("data", content);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "发布失败：" + e.getMessage());
        }
        return result;
    }

    private void saveContentTags(Long contentId, List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }
        for (String tagName : tagNames) {
            if (tagName == null || tagName.trim().isEmpty()) {
                continue;
            }
            String sql = "SELECT id FROM tag WHERE name = ? AND status = 1";
            try {
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, tagName.trim());
                if (!rows.isEmpty()) {
                    Long tagId = ((Number) rows.get(0).get("id")).longValue();
                    String insertSql = "INSERT INTO content_tag (content_id, tag_id, create_time) VALUES (?, ?, NOW())";
                    jdbcTemplate.update(insertSql, contentId, tagId);
                }
            } catch (Exception e) {
            }
        }
    }

    // 获取内容列表
    @GetMapping("/list")
    public Map<String, Object> getContentList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String tags, // 支持前端传递的tags参数
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            String statusStr = status != null ? String.valueOf(status) : null;
            List<Content> list = contentService.search(null, null, null, statusStr, page, pageSize);
            
            // 按类型筛选
            if (type != null) {
                list = list.stream().filter(c -> c.getType().equals(type)).toList();
            }
            
            // 按标签筛选（支持tag和tags两种参数名，同时匹配标题和描述）
            String filterTag = tags != null && !tags.isEmpty() ? tags : tag;
            if (filterTag != null && !filterTag.isEmpty()) {
                list = list.stream().filter(c -> {
                    try {
                        // 检查标签字段
                        String tagList = c.getTags();
                        if (tagList != null && tagList.contains(filterTag)) {
                            return true;
                        }
                        // 检查标题
                        if (c.getTitle() != null && c.getTitle().contains(filterTag)) {
                            return true;
                        }
                        // 检查描述
                        if (c.getDescription() != null && c.getDescription().contains(filterTag)) {
                            return true;
                        }
                        // 检查关联的标签名称
                        List<String> tagNames = getContentTagNames(c.getId());
                        for (String tagName : tagNames) {
                            if (tagName.contains(filterTag)) {
                                return true;
                            }
                        }
                        return false;
                    } catch (Exception e) {
                        return false;
                    }
                }).toList();
            }
            
            // 按地点筛选
            if (location != null && !location.isEmpty()) {
                list = list.stream().filter(c -> {
                    try {
                        return c.getLocation() != null && c.getLocation().contains(location);
                    } catch (Exception e) {
                        return false;
                    }
                }).toList();
            }
            
            // 按距离排序（简单实现，实际项目中需要使用地理位置计算）
            if (latitude != null && longitude != null) {
                // 这里可以实现基于地理位置的距离计算和排序
                // 由于没有具体的地理位置数据，暂时跳过
            }
            
            // 为每个内容关联标签信息
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Content c : list) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", c.getId());
                item.put("userId", c.getUserId());
                item.put("title", c.getTitle());
                item.put("description", c.getDescription());
                item.put("coverImage", c.getCoverImage());
                item.put("location", c.getLocation());
                item.put("locationId", c.getLocationId());
                item.put("priceType", c.getPriceType());
                item.put("price", c.getPrice());
                item.put("deposit", c.getDeposit());
                item.put("type", c.getType());
                item.put("status", c.getStatus());
                item.put("tags", c.getTags());
                item.put("viewCount", c.getViewCount());
                item.put("likeCount", c.getLikeCount());
                item.put("commentCount", c.getCommentCount());
                item.put("createTime", c.getCreateTime());
                item.put("updateTime", c.getUpdateTime());
                // 添加标签名称列表
                item.put("tagNames", getContentTagNames(c.getId()));
                resultList.add(item);
            }
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", resultList);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    // 获取内容关联的标签名称列表
    private List<String> getContentTagNames(Long contentId) {
        List<String> tagNames = new ArrayList<>();
        try {
            String sql = "SELECT t.name FROM tag t " +
                        "INNER JOIN content_tag ct ON t.id = ct.tag_id " +
                        "WHERE ct.content_id = ? AND t.status = 1";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, contentId);
            for (Map<String, Object> row : rows) {
                tagNames.add((String) row.get("name"));
            }
        } catch (Exception e) {
            // 忽略异常，返回空列表
        }
        return tagNames;
    }

    // 获取用户的内容列表
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserContent(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Content> list = contentService.findByUserId(userId, page, pageSize);
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", list);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取内容详情 - 通过路径参数
    @GetMapping("/{id}")
    public Map<String, Object> getContentDetail(@PathVariable Long id) {
        return getContentById(id);
    }
    
    // 获取内容详情 - 通过查询参数
    @GetMapping("/info")
    public Map<String, Object> getContentInfo(@RequestParam Long id) {
        return getContentById(id);
    }
    
    // 内部方法：获取内容详情
    private Map<String, Object> getContentById(Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Content content = contentService.findById(id);
            if (content != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", content);
            } else {
                result.put("code", 404);
                result.put("message", "内容不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 删除内容
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteContent(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            contentService.deleteById(id);
            result.put("code", 200);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    // 搜索作品
    @GetMapping("/search")
    public Map<String, Object> searchContent(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            String statusStr = status != null ? String.valueOf(status) : "1";
            List<Content> list = contentService.search(null, keyword, null, statusStr, page, pageSize);
            
            // 为每个内容关联标签信息
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Content c : list) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", c.getId());
                item.put("userId", c.getUserId());
                item.put("title", c.getTitle());
                item.put("coverImage", c.getCoverImage());
                item.put("description", c.getDescription());
                item.put("viewCount", c.getViewCount());
                item.put("likeCount", c.getLikeCount());
                item.put("commentCount", c.getCommentCount());
                item.put("tagNames", getContentTagNames(c.getId()));
                resultList.add(item);
            }
            
            result.put("code", 200);
            result.put("message", "搜索成功");
            result.put("data", resultList);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }
}

// 分类和轮播图控制器
@RestController
@RequestMapping("/api")
class ApiCategoryBannerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 获取分类列表
    @GetMapping("/category/list")
    public Map<String, Object> getCategoryList() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从数据库查询分类列表，添加 type 字段（0 表示风格标签）
            String sql = "SELECT id, name, '标签分类' as description, status, 0 as type FROM tag WHERE status = 1 ORDER BY id ASC";
            List<Map<String, Object>> categories = jdbcTemplate.queryForList(sql);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", categories);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 获取轮播图列表
    @GetMapping("/banner/list")
    public Map<String, Object> getBannerList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> banners = new ArrayList<>();
            
            try {
                // 从数据库查询轮播图配置
                String sql = "SELECT config_value FROM system_config WHERE config_key = 'banner'";
                String bannerJson = jdbcTemplate.queryForObject(sql, String.class);
                
                if (bannerJson != null && !bannerJson.isEmpty()) {
                    // 解析JSON数组
                    ObjectMapper objectMapper = new ObjectMapper();
                    banners = objectMapper.readValue(bannerJson, new TypeReference<List<Map<String, Object>>>() {});
                    
                    // 将image字段转换为imageUrl，适配小程序端
                    for (Map<String, Object> banner : banners) {
                        if (banner.containsKey("image")) {
                            banner.put("imageUrl", banner.get("image"));
                            banner.remove("image");
                        }
                    }
                }
            } catch (Exception e) {
                // 轮播图配置不存在或解析失败，使用热度高的作品图片作为轮播图
                System.out.println("轮播图配置不存在或解析失败，使用热度高的作品图片作为轮播图");
            }
            
            // 如果轮播图为空，使用热度高的作品图片作为轮播图
            if (banners == null || banners.isEmpty()) {
                // 从数据库查询热度高的作品
                // 使用协同算法计算热度：view_count * 0.4 + like_count * 0.4 + comment_count * 0.2
                // 如果热度相同，按创建时间倒序排序
                String sql = "SELECT id, title, cover_image, view_count, like_count, comment_count " +
                        "FROM content " +
                        "WHERE status = 1 AND type = 1 AND cover_image IS NOT NULL AND cover_image != '' " +
                        "ORDER BY (view_count * 0.4 + like_count * 0.4 + comment_count * 0.2) DESC, create_time DESC " +
                        "LIMIT 5";
                List<Map<String, Object>> hotContents = jdbcTemplate.queryForList(sql);
                
                // 将热度高的作品转换为轮播图格式
                for (int i = 0; i < hotContents.size(); i++) {
                    Map<String, Object> content = hotContents.get(i);
                    Map<String, Object> banner = new HashMap<>();
                    banner.put("id", i + 1);
                    banner.put("status", 1);
                    banner.put("title", content.get("title"));
                    banner.put("imageUrl", content.get("cover_image"));
                    banner.put("contentId", content.get("id"));
                    banners.add(banner);
                }
                
                // 如果仍然没有轮播图数据，使用默认轮播图
                if (banners.isEmpty()) {
                    // 创建默认轮播图
                    List<Map<String, Object>> defaultBanners = new ArrayList<>();
                    Map<String, Object> banner1 = new HashMap<>();
                    banner1.put("id", 1);
                    banner1.put("status", 1);
                    banner1.put("title", "专业人像摄影服务");
                    banner1.put("imageUrl", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=750&h=300&fit=crop");
                    banner1.put("contentId", 1);
                    defaultBanners.add(banner1);
                    
                    Map<String, Object> banner2 = new HashMap<>();
                    banner2.put("id", 2);
                    banner2.put("status", 1);
                    banner2.put("title", "自然风光摄影");
                    banner2.put("imageUrl", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=750&h=300&fit=crop");
                    banner2.put("contentId", 2);
                    defaultBanners.add(banner2);
                    
                    Map<String, Object> banner3 = new HashMap<>();
                    banner3.put("id", 3);
                    banner3.put("status", 1);
                    banner3.put("title", "婚礼摄影服务");
                    banner3.put("imageUrl", "https://images.unsplash.com/photo-1508804185872-d7badad00f7d?w=750&h=300&fit=crop");
                    banner3.put("contentId", 3);
                    defaultBanners.add(banner3);
                    
                    banners = defaultBanners;
                }
            } else {
                // 为每个轮播图添加id和status字段
                for (int i = 0; i < banners.size(); i++) {
                    Map<String, Object> banner = banners.get(i);
                    banner.put("id", i + 1);
                    banner.put("status", 1);
                    if (!banner.containsKey("title")) {
                        banner.put("title", "轮播图" + (i + 1));
                    }
                }
            }
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", banners);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}
