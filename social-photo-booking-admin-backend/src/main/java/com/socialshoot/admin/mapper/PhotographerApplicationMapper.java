package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.PhotographerApplication;
import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;

import java.util.List;

@Mapper
public interface PhotographerApplicationMapper {
    
    @Insert("INSERT INTO photographer_application (user_id, real_name, id_number, phone, styles, portfolio, status, create_time, update_time) " +
            "VALUES (#{userId}, #{realName}, #{idNumber}, #{phone}, #{styles}, #{portfolio}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PhotographerApplication application);
    
    @Update("UPDATE photographer_application SET real_name = #{realName}, id_number = #{idNumber}, phone = #{phone}, styles = #{styles}, " +
            "portfolio = #{portfolio}, status = #{status}, reject_reason = #{rejectReason}, update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void update(PhotographerApplication application);
    
    @Select("SELECT id, user_id as userId, real_name as realName, id_number as idNumber, phone, styles, portfolio, status, reject_reason as rejectReason, create_time as createTime, update_time as updateTime FROM photographer_application WHERE user_id = #{userId}")
    PhotographerApplication findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT id, user_id as userId, real_name as realName, id_number as idNumber, phone, styles, portfolio, status, reject_reason as rejectReason, create_time as createTime, update_time as updateTime FROM photographer_application WHERE id = #{id}")
    PhotographerApplication findById(@Param("id") Long id);
    
    @Select("SELECT id, user_id as userId, real_name as realName, id_number as idNumber, phone, styles, portfolio, status, reject_reason as rejectReason, create_time as createTime, update_time as updateTime FROM photographer_application WHERE status = 0 ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<PhotographerApplication> findPending(@Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM photographer_application WHERE status = 0")
    int countPending();
    
    @Update("UPDATE photographer_application SET status = 1, update_time = NOW() WHERE id = #{id}")
    void approve(@Param("id") Long id, @Param("auditorId") Long auditorId, @Param("auditTime") LocalDateTime auditTime);
    
    @Update("UPDATE photographer_application SET status = 2, reject_reason = #{rejectReason}, update_time = NOW() WHERE id = #{id}")
    void reject(@Param("id") Long id, @Param("rejectReason") String rejectReason, @Param("auditorId") Long auditorId, @Param("auditTime") LocalDateTime auditTime);
}