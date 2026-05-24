package com.photo.booking.service;

import com.photo.booking.entity.Photographer;

import java.math.BigDecimal;
import java.util.List;

public interface PhotographerService {
    Photographer getPhotographerById(Long id);
    Photographer getPhotographerByUserId(Long userId);
    List<Photographer> getAllPhotographers();
    List<Photographer> getPhotographersByRegion(String region);
    List<Photographer> getPhotographersByStyle(String style);
    List<Photographer> getPhotographersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<Photographer> getPhotographersByCertified(Integer certified);
    Photographer createPhotographer(Photographer photographer);
    Photographer updatePhotographer(Photographer photographer);
    void updatePhotographerOrders(Long id, Integer orders);
    void updatePhotographerRating(Long id, BigDecimal rating);
    void updatePhotographerStatus(Long id, Integer status);
    List<Photographer> getHotPhotographers(Integer limit);
}