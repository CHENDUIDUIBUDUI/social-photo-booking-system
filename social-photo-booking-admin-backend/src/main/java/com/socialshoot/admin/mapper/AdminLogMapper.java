package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.AdminLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminLogMapper {
    int insert(AdminLog adminLog);
}