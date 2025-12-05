package com.example.demo.service;

import com.example.demo.entity.Tour;
import com.example.demo.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }

    @Transactional
    public Tour saveTour(Tour tour) {
        return tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(Long id) {
        tourRepository.deleteById(id);
    }

    public List<Tour> searchTours(String location, BigDecimal minPrice, BigDecimal maxPrice, Integer numberOfDays) {
        return tourRepository.findToursWithFilters(location, minPrice, maxPrice, numberOfDays);
    }

    public List<Tour> getUpcomingTours() {
        return tourRepository.findUpcomingTours(java.time.LocalDate.now());
    }
}



