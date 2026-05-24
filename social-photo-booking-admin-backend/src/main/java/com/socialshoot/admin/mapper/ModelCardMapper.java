package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.ModelCard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ModelCardMapper {
    
    @Insert("INSERT INTO model_card (user_id, name, height, weight, styles, is_paid, availability, portfolio, introduction, status, reject_reason, create_time, update_time) " +
            "VALUES (#{userId}, #{name}, #{height}, #{weight}, #{styles}, #{isPaid}, #{availability}, #{portfolio}, #{introduction}, #{status}, #{rejectReason}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ModelCard modelCard);
    
    @Update("UPDATE model_card SET name = #{name}, height = #{height}, weight = #{weight}, styles = #{styles}, is_paid = #{isPaid}, " +
            "availability = #{availability}, portfolio = #{portfolio}, introduction = #{introduction}, status = #{status}, reject_reason = #{rejectReason}, update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void update(ModelCard modelCard);
    
    @Select("SELECT id, user_id as userId, name, height, weight, styles, is_paid as isPaid, availability, portfolio, introduction, status, reject_reason as rejectReason, create_time as createTime, update_time as updateTime FROM model_card WHERE user_id = #{userId}")
    ModelCard findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT id, user_id as userId, name, height, weight, styles, is_paid as isPaid, availability, portfolio, introduction, status, reject_reason as rejectReason, create_time as createTime, update_time as updateTime FROM model_card WHERE id = #{id}")
    ModelCard findById(@Param("id") Long id);
    
    @Select("SELECT id, user_id as userId, name, height, weight, styles, is_paid as isPaid, availability, portfolio, introduction, status, reject_reason as rejectReason, create_time as createTime, update_time as updateTime FROM model_card WHERE status = 1 ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<ModelCard> findAll(@Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT id, user_id as userId, name, height, weight, styles, is_paid as isPaid, availability, portfolio, introduction, status, reject_reason as rejectReason, create_time as createTime, update_time as updateTime FROM model_card WHERE status = 0 ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<ModelCard> findPending(@Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM model_card WHERE status = 0")
    int countPending();
    
    @Select("SELECT COUNT(*) FROM model_card")
    int countAll();
    
    @Update("UPDATE model_card SET status = 1, update_time = NOW() WHERE id = #{id}")
    void approve(@Param("id") Long id, @Param("auditorId") Long auditorId);
    
    @Update("UPDATE model_card SET status = 2, reject_reason = #{rejectReason}, update_time = NOW() WHERE id = #{id}")
    void reject(@Param("id") Long id, @Param("rejectReason") String rejectReason, @Param("auditorId") Long auditorId);
}