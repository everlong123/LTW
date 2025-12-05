package com.example.demo.repository;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTour(Tour tour);
    
    List<Booking> findByTourId(Long tourId);
    
    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.tour WHERE b.user.id = :userId ORDER BY b.bookingDate DESC")
    List<Booking> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.tour WHERE b.customerEmail = :email ORDER BY b.bookingDate DESC")
    List<Booking> findByCustomerEmail(@Param("email") String email);
    
    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.tour ORDER BY b.bookingDate DESC")
    List<Booking> findAllOrderByBookingDateDesc();
    
    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.tour LEFT JOIN FETCH b.user WHERE b.id = :id")
    Optional<Booking> findByIdWithTour(@Param("id") Long id);
}



