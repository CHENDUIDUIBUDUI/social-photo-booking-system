package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.Announcement;
import com.socialshoot.admin.mapper.AnnouncementMapper;
import com.socialshoot.admin.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public Map<String, Object> getAnnouncements(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        List<Announcement> list = announcementMapper.selectAnnouncements(params);
        int total = announcementMapper.countAnnouncements(params);
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Override
    public Announcement getAnnouncementById(Long id) {
        return announcementMapper.selectAnnouncementById(id);
    }

    @Override
    public boolean addAnnouncement(Announcement announcement) {
        return announcementMapper.insertAnnouncement(announcement) > 0;
    }

    @Override
    public boolean updateAnnouncement(Announcement announcement) {
        return announcementMapper.updateAnnouncement(announcement) > 0;
    }

    @Override
    public boolean deleteAnnouncement(Long id) {
        return announcementMapper.deleteAnnouncement(id) > 0;
    }
}
