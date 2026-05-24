package com.socialshoot.admin.service.impl;

import com.socialshoot.admin.entity.AdminLog;
import com.socialshoot.admin.mapper.AdminLogMapper;
import com.socialshoot.admin.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminLogServiceImpl implements AdminLogService {

    @Autowired
    private AdminLogMapper adminLogMapper;

    @Override
    public void addLog(AdminLog adminLog) {
        adminLogMapper.insert(adminLog);
    }
}