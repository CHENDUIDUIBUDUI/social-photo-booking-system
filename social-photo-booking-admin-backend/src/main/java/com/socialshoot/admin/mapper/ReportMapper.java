package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

@Mapper
public interface ReportMapper {
    @Select("SELECT id, reporter_id AS reporterId, type, target_id AS targetId, reason, images, status, " +
            "handle_result AS handleResult, handler_id AS handlerId, handle_time AS handleTime, create_time AS createTime " +
            "FROM report WHERE 1=1 " +
            "AND (#{status} IS NULL OR status = #{status}) " +
            "AND (#{type} IS NULL OR type = #{type}) " +
            "ORDER BY create_time DESC")
    List<Report> selectByCondition(@Param("status") Integer status, @Param("type") Integer type);

    @Select("SELECT id, reporter_id AS reporterId, type, target_id AS targetId, reason, images, status, " +
            "handle_result AS handleResult, handler_id AS handlerId, handle_time AS handleTime, create_time AS createTime " +
            "FROM report WHERE id = #{id}")
    Report selectById(@Param("id") Long id);

    @Update("UPDATE report SET status = #{status}, handle_result = #{handleResult}, " +
            "handler_id = #{handlerId}, handle_time = NOW() WHERE id = #{id}")
    int updateHandle(@Param("id") Long id, @Param("status") Integer status, 
                   @Param("handleResult") String handleResult, @Param("handlerId") Long handlerId);

    @Insert("INSERT INTO report (reporter_id, type, target_id, reason, images, status, create_time) " +
            "VALUES (#{reporterId}, #{type}, #{targetId}, #{reason}, #{images}, #{status}, NOW())")
    int insert(Report report);

    @Select("<script>" +
            "SELECT COUNT(*) FROM report " +
            "<where>" +
            "<if test='status != null'>status = #{status}</if>" +
            "</where>" +
            "</script>")
    int countByStatus(@Param("status") Integer status);

    @Select("<script>" +
            "SELECT COUNT(*) FROM report " +
            "<where>" +
            "<if test='type != null'>type = #{type}</if>" +
            "</where>" +
            "</script>")
    int countByType(@Param("type") Integer type);
}
