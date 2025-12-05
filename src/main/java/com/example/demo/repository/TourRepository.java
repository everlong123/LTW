package com.example.demo.repository;

import com.example.demo.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByLocationContainingIgnoreCase(String location);
    
    List<Tour> findByNumberOfDays(Integer numberOfDays);
    
    @Query("SELECT t FROM Tour t WHERE t.price BETWEEN :minPrice AND :maxPrice")
    List<Tour> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT t FROM Tour t WHERE t.departureDate >= :date ORDER BY t.departureDate ASC")
    List<Tour> findUpcomingTours(@Param("date") LocalDate date);
    
    @Query("SELECT t FROM Tour t WHERE " +
           "(:location IS NULL OR LOWER(t.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:minPrice IS NULL OR t.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR t.price <= :maxPrice) AND " +
           "(:numberOfDays IS NULL OR t.numberOfDays = :numberOfDays)")
    List<Tour> findToursWithFilters(
        @Param("location") String location,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("numberOfDays") Integer numberOfDays
    );
}



