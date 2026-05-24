package com.photo.booking.service.impl;

import com.photo.booking.entity.Package;
import com.photo.booking.mapper.PackageMapper;
import com.photo.booking.service.PackageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {
    
    @Resource
    private PackageMapper packageMapper;
    
    @Override
    public List<Package> getPackagesByPhotographerId(Long photographerId) {
        return packageMapper.getPackagesByPhotographerId(photographerId);
    }
    
    @Override
    public Package getPackageById(Long id) {
        return packageMapper.getPackageById(id);
    }
    
    @Override
    public Package createPackage(Package pack) {
        packageMapper.insert(pack);
        return packageMapper.getPackageById(pack.getId());
    }
    
    @Override
    public Package updatePackage(Package pack) {
        packageMapper.update(pack);
        return packageMapper.getPackageById(pack.getId());
    }
    
    @Override
    public void deletePackage(Long id) {
        packageMapper.delete(id);
    }
}