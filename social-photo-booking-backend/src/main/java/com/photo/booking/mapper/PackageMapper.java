package com.photo.booking.mapper;

import com.photo.booking.entity.Package;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PackageMapper {
    // 根据摄影师ID获取套餐列表
    List<Package> getPackagesByPhotographerId(Long photographerId);
    
    // 根据ID获取套餐详情
    Package getPackageById(Long id);
    
    // 创建套餐
    int insert(Package pack);
    
    // 更新套餐
    int update(Package pack);
    
    // 删除套餐
    int delete(Long id);
}