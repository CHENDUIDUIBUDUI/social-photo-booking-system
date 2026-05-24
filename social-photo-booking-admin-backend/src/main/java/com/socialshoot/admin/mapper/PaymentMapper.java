package com.socialshoot.admin.mapper;

import com.socialshoot.admin.entity.Payment;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PaymentMapper {
    
    @Insert("INSERT INTO payment (order_no, user_id, amount, transaction_id, status, pay_type, pay_time, create_time, update_time) " +
            "VALUES (#{orderNo}, #{userId}, #{amount}, #{transactionId}, #{status}, #{payType}, #{payTime}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Payment payment);
    
    @Update("UPDATE payment SET transaction_id = #{transactionId}, status = #{status}, pay_time = #{payTime}, update_time = #{updateTime} " +
            "WHERE order_no = #{orderNo}")
    void updateByOrderNo(Payment payment);
    
    @Select("SELECT id, order_no as orderNo, user_id as userId, amount, transaction_id as transactionId, status, pay_type as payType, pay_time as payTime, create_time as createTime, update_time as updateTime FROM payment WHERE order_no = #{orderNo}")
    Payment findByOrderNo(@Param("orderNo") String orderNo);
    
    @Select("SELECT id, order_no as orderNo, user_id as userId, amount, transaction_id as transactionId, status, pay_type as payType, pay_time as payTime, create_time as createTime, update_time as updateTime FROM payment WHERE transaction_id = #{transactionId}")
    Payment findByTransactionId(@Param("transactionId") String transactionId);
}