package com.photo.booking.mapper;

import com.photo.booking.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemConfigMapper {
    SystemConfig selectByKey(@Param("key") String key);
    List<SystemConfig> selectAll();
    int insert(SystemConfig systemConfig);
    int update(SystemConfig systemConfig);
    int delete(@Param("key") String key);
}
