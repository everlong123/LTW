package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Tour;
import com.example.demo.entity.User;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.TourRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Booking createBooking(Booking booking, Long tourId, Long userId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        // Kiểm tra số lượng khách còn lại
        int availableSlots = tour.getAvailableSlots();
        if (booking.getNumberOfPeople() > availableSlots) {
            throw new RuntimeException("Số lượng khách vượt quá số chỗ còn lại. Chỗ còn lại: " + availableSlots);
        }

        // Tính tổng giá
        BigDecimal totalPrice = tour.getPrice().multiply(BigDecimal.valueOf(booking.getNumberOfPeople()));
        booking.setTotalPrice(totalPrice);
        booking.setTour(tour);

        // Set user - ưu tiên userId, nếu không có thì tìm theo email
        User userToSet = null;
        if (userId != null) {
            userToSet = userRepository.findById(userId).orElse(null);
        }
        
        // Nếu không tìm được user theo userId, thử tìm theo email
        if (userToSet == null && booking.getCustomerEmail() != null && !booking.getCustomerEmail().isEmpty()) {
            userToSet = userRepository.findByEmail(booking.getCustomerEmail()).orElse(null);
        }
        
        // Set user vào booking - BẮT BUỘC phải có user
        if (userToSet != null) {
            booking.setUser(userToSet);
        } else {
            throw new RuntimeException("Không thể xác định người dùng. Vui lòng đăng nhập lại.");
        }

        Booking savedBooking = bookingRepository.save(booking);
        bookingRepository.flush(); // Đảm bảo flush vào database ngay lập tức
        
        // Reload booking để đảm bảo có đầy đủ thông tin (tour, user)
        savedBooking = bookingRepository.findByIdWithTour(savedBooking.getId()).orElse(savedBooking);

        return savedBooking;
    }

    public List<Booking> getBookingsByTour(Long tourId) {
        return bookingRepository.findByTourId(tourId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllOrderByBookingDateDesc();
    }

    public List<Booking> getBookingsByUser(Long userId) {
        if (userId != null) {
            return bookingRepository.findByUserId(userId);
        }
        return List.of();
    }
    
    public List<Booking> getBookingsByEmail(String email) {
        if (email != null && !email.isEmpty()) {
            return bookingRepository.findByCustomerEmail(email);
        }
        return List.of();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findByIdWithTour(id);
    }

    @Transactional
    public Booking updatePaymentStatus(Long bookingId, Boolean paymentStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setPaymentStatus(paymentStatus);
        return bookingRepository.save(booking);
    }

}



