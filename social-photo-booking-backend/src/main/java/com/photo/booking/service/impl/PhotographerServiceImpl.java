package com.photo.booking.service.impl;

import com.photo.booking.entity.Photographer;
import com.photo.booking.mapper.PhotographerMapper;
import com.photo.booking.mapper.UserMapper;
import com.photo.booking.service.PhotographerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PhotographerServiceImpl implements PhotographerService {

    @Resource
    private PhotographerMapper photographerMapper;
    
    @Resource
    private UserMapper userMapper;

    @Override
    public Photographer getPhotographerById(Long id) {
        return photographerMapper.selectById(id);
    }

    @Override
    public Photographer getPhotographerByUserId(Long userId) {
        return photographerMapper.selectByUserId(userId);
    }

    @Override
    public List<Photographer> getAllPhotographers() {
        return photographerMapper.selectAll();
    }

    @Override
    public List<Photographer> getPhotographersByRegion(String region) {
        return photographerMapper.selectByRegion(region);
    }

    @Override
    public List<Photographer> getPhotographersByStyle(String style) {
        return photographerMapper.selectByStyle(style);
    }

    @Override
    public List<Photographer> getPhotographersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return photographerMapper.selectByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Photographer> getPhotographersByCertified(Integer certified) {
        return photographerMapper.selectByCertified(certified);
    }

    @Override
    public Photographer createPhotographer(Photographer photographer) {
        // 插入摄影师记录
        photographerMapper.insert(photographer);
        // 更新用户角色为摄影师
        if (photographer.getUserId() != null) {
            userMapper.updateRole(photographer.getUserId(), 1);
        }
        return photographer;
    }

    @Override
    public Photographer updatePhotographer(Photographer photographer) {
        photographerMapper.update(photographer);
        return photographer;
    }

    @Override
    public void updatePhotographerOrders(Long id, Integer orders) {
        photographerMapper.updateOrders(id, orders);
    }

    @Override
    public void updatePhotographerRating(Long id, BigDecimal rating) {
        photographerMapper.updateRating(id, rating);
    }

    @Override
    public void updatePhotographerStatus(Long id, Integer status) {
        photographerMapper.updateStatus(id, status);
    }
    
    @Override
    public List<Photographer> getHotPhotographers(Integer limit) {
        return photographerMapper.selectHot(limit);
    }
}