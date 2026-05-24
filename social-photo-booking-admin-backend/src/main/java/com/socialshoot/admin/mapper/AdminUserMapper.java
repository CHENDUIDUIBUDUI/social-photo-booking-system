package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface AdminUserMapper {
    @Select("SELECT * FROM admin_user WHERE username = #{username}")
    AdminUser selectByUsername(@Param("username") String username);
    
    @Select("SELECT * FROM admin_user WHERE id = #{id}")
    AdminUser selectById(@Param("id") Long id);
    
    @Select("SELECT * FROM admin_user")
    List<AdminUser> selectAll();
    
    @Insert("INSERT INTO admin_user (username, password, real_name, avatar, role, status, create_time, update_time) VALUES (#{username}, #{password}, #{realName}, #{avatar}, #{role}, #{status}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    int insert(AdminUser adminUser);
    
    @Update("UPDATE admin_user SET real_name = #{realName}, avatar = #{avatar}, role = #{role}, status = #{status}, update_time = NOW() WHERE id = #{id}")
    int update(AdminUser adminUser);
    
    @Update("UPDATE admin_user SET last_login_time = #{lastLoginTime}, last_login_ip = #{lastLoginIp}, update_time = NOW() WHERE id = #{id}")
    int updateLastLogin(@Param("id") Long id, @Param("lastLoginTime") String lastLoginTime, @Param("lastLoginIp") String lastLoginIp);
    
    @Delete("DELETE FROM admin_user WHERE id = #{id}")
    int delete(@Param("id") Long id);
}