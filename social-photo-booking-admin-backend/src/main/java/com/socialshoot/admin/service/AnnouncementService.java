package com.socialshoot.admin.service;

import com.socialshoot.admin.entity.Announcement;

import java.util.Map;

public interface AnnouncementService {
    Map<String, Object> getAnnouncements(Map<String, Object> params);
    Announcement getAnnouncementById(Long id);
    boolean addAnnouncement(Announcement announcement);
    boolean updateAnnouncement(Announcement announcement);
    boolean deleteAnnouncement(Long id);
}
