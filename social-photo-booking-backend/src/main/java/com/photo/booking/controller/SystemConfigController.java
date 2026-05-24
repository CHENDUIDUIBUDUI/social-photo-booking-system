package com.photo.booking.controller;

import com.photo.booking.entity.SystemConfig;
import com.photo.booking.service.SystemConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/config")
public class SystemConfigController {

    @Resource
    private SystemConfigService systemConfigService;

    @GetMapping("/get")
    public SystemConfig getSystemConfig(@RequestParam String key) {
        return systemConfigService.getSystemConfig(key);
    }

    @GetMapping("/list")
    public List<SystemConfig> getAllSystemConfig() {
        return systemConfigService.getAllSystemConfig();
    }

    @PostMapping("/create")
    public SystemConfig createSystemConfig(@RequestBody SystemConfig systemConfig) {
        return systemConfigService.createSystemConfig(systemConfig);
    }

    @PutMapping("/update")
    public SystemConfig updateSystemConfig(@RequestBody SystemConfig systemConfig) {
        return systemConfigService.updateSystemConfig(systemConfig);
    }

    @DeleteMapping("/delete")
    public void deleteSystemConfig(@RequestParam String key) {
        systemConfigService.deleteSystemConfig(key);
    }
}
