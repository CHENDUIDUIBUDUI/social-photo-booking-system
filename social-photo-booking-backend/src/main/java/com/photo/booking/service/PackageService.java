package com.photo.booking.service;

import com.photo.booking.entity.Package;
import java.util.List;

public interface PackageService {
    // 根据摄影师ID获取套餐列表
    List<Package> getPackagesByPhotographerId(Long photographerId);
    
    // 根据ID获取套餐详情
    Package getPackageById(Long id);
    
    // 创建套餐
    Package createPackage(Package pack);
    
    // 更新套餐
    Package updatePackage(Package pack);
    
    // 删除套餐
    void deletePackage(Long id);
}