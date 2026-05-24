package com.photo.booking.service;

import com.photo.booking.entity.SystemConfig;

import java.util.List;

public interface SystemConfigService {
    SystemConfig getSystemConfig(String key);
    List<SystemConfig> getAllSystemConfig();
    SystemConfig createSystemConfig(SystemConfig systemConfig);
    SystemConfig updateSystemConfig(SystemConfig systemConfig);
    void deleteSystemConfig(String key);
}
