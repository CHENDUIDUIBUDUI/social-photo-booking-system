package com.photo.booking.mapper;

import com.photo.booking.entity.Photographer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhotographerMapper {
    Photographer selectById(@Param("id") Long id);
    Photographer selectByUserId(@Param("userId") Long userId);
    List<Photographer> selectAll();
    List<Photographer> selectByRegion(@Param("region") String region);
    List<Photographer> selectByStyle(@Param("style") String style);
    List<Photographer> selectByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, @Param("maxPrice") java.math.BigDecimal maxPrice);
    List<Photographer> selectByCertified(@Param("certified") Integer certified);
    int insert(Photographer photographer);
    int update(Photographer photographer);
    int updateOrders(@Param("id") Long id, @Param("orders") Integer orders);
    int updateRating(@Param("id") Long id, @Param("rating") java.math.BigDecimal rating);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    List<Photographer> selectHot(@Param("limit") Integer limit);
}