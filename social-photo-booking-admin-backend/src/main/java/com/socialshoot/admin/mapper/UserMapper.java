package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT id, open_id as openId, union_id as unionId, nickname, avatar, phone, role_id as roleId, credit_score as creditScore, status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime FROM user WHERE 1=1 " +
            "AND (#{nickname} IS NULL OR #{nickname} = '' OR nickname LIKE CONCAT('%', #{nickname}, '%')) " +
            "AND (#{phone} IS NULL OR #{phone} = '' OR phone LIKE CONCAT('%', #{phone}, '%')) " +
            "AND (#{role} IS NULL OR role_id = #{role}) " +
            "ORDER BY create_time DESC")
    List<User> selectByCondition(@Param("nickname") String nickname, @Param("phone") String phone, @Param("role") Integer role);

    @Select("SELECT id, open_id as openId, union_id as unionId, nickname, avatar, phone, role_id as roleId, credit_score as creditScore, status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime FROM user WHERE id = #{id}")
    User selectById(@Param("id") Long id);

    @Select("SELECT id, open_id as openId, union_id as unionId, nickname, avatar, phone, role_id as roleId, credit_score as creditScore, status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime FROM user WHERE open_id = #{openId}")
    User selectByOpenId(@Param("openId") String openId);

    @Select("SELECT id, open_id as openId, union_id as unionId, nickname, avatar, phone, role_id as roleId, credit_score as creditScore, status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime FROM user WHERE phone = #{phone}")
    User selectByPhone(@Param("phone") String phone);

    @Insert("INSERT INTO user (open_id, nickname, avatar, phone, role_id, credit_score, status, create_time, update_time) " +
            "VALUES (#{openId}, #{nickname}, #{avatar}, #{phone}, #{roleId}, #{creditScore}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Update("UPDATE user SET nickname = #{nickname}, avatar = #{avatar}, update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void update(User user);

    @Update("UPDATE user SET phone = #{phone}, update_time = #{updateTime} WHERE id = #{id}")
    int updatePhone(@Param("id") Long id, @Param("phone") String phone);

    @Update("UPDATE user SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE user SET role_id = #{roleId}, update_time = NOW() WHERE id = #{id}")
    int updateRoleId(@Param("id") Long id, @Param("roleId") Integer roleId);

    @Select("SELECT COUNT(*) FROM user")
    int countAllUsers();

    @Select("SELECT COUNT(*) FROM user WHERE role_id = #{role}")
    int countByRole(@Param("role") Integer role);

    @Select("SELECT COUNT(*) FROM user WHERE role_id = 1")
    int countPhotographers();

    @Select("SELECT id, open_id as openId, union_id as unionId, nickname, avatar, phone, role_id as roleId, credit_score as creditScore, status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime FROM user WHERE role_id = 1 ORDER BY credit_score DESC LIMIT #{limit}")
    List<User> selectHotPhotographers(@Param("limit") int limit);

    @Select("SELECT id, open_id as openId, union_id as unionId, nickname, avatar, phone, role_id as roleId, credit_score as creditScore, specialty, status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime FROM user")
    List<User> selectAll();
    
    @Select("SELECT id, open_id as openId, union_id as unionId, nickname, avatar, phone, role_id as roleId, credit_score as creditScore, specialty, status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime FROM user " +
            "WHERE role_id = 1 AND (nickname LIKE CONCAT('%', #{keyword}, '%') OR specialty LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY create_time DESC")
    List<User> search(@Param("keyword") String keyword);
}
