package com.socialshoot.admin.controller;

import com.socialshoot.admin.entity.Announcement;
import com.socialshoot.admin.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/notice")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/list")
    public Map<String, Object> getAnnouncements(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", (pageNum - 1) * pageSize);
        params.put("pageSize", pageSize);
        params.put("id", id);
        params.put("title", title);

        Map<String, Object> result = announcementService.getAnnouncements(params);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", result);
        return response;
    }

    @GetMapping("/detail")
    public Map<String, Object> getAnnouncement(@RequestParam Long id) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        Map<String, Object> response = new HashMap<>();
        if (announcement != null) {
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", announcement);
        } else {
            response.put("code", 404);
            response.put("message", "公告不存在");
        }
        return response;
    }

    @PostMapping("/add")
    public Map<String, Object> addAnnouncement(@RequestBody Announcement announcement) {
        boolean success = announcementService.addAnnouncement(announcement);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "添加成功");
        } else {
            response.put("code", 500);
            response.put("message", "添加失败");
        }
        return response;
    }

    @PutMapping("/update")
    public Map<String, Object> updateAnnouncement(@RequestBody Announcement announcement) {
        boolean success = announcementService.updateAnnouncement(announcement);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "更新成功");
        } else {
            response.put("code", 500);
            response.put("message", "更新失败");
        }
        return response;
    }

    @DeleteMapping("/delete")
    public Map<String, Object> deleteAnnouncement(@RequestParam Long id) {
        boolean success = announcementService.deleteAnnouncement(id);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "删除成功");
        } else {
            response.put("code", 500);
            response.put("message", "删除失败");
        }
        return response;
    }
}
