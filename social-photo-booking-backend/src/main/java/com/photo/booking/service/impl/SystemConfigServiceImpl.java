package com.photo.booking.service.impl;

import com.photo.booking.entity.SystemConfig;
import com.photo.booking.mapper.SystemConfigMapper;
import com.photo.booking.service.SystemConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Resource
    private SystemConfigMapper systemConfigMapper;

    @Override
    public SystemConfig getSystemConfig(String key) {
        return systemConfigMapper.selectByKey(key);
    }

    @Override
    public List<SystemConfig> getAllSystemConfig() {
        return systemConfigMapper.selectAll();
    }

    @Override
    public SystemConfig createSystemConfig(SystemConfig systemConfig) {
        systemConfigMapper.insert(systemConfig);
        return systemConfig;
    }

    @Override
    public SystemConfig updateSystemConfig(SystemConfig systemConfig) {
        systemConfigMapper.update(systemConfig);
        return systemConfig;
    }

    @Override
    public void deleteSystemConfig(String key) {
        systemConfigMapper.delete(key);
    }
}
