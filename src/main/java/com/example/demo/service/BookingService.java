package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Tour;
import com.example.demo.entity.User;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.TourRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private JavaMailSender mailSender;

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

        // Set user nếu userId được cung cấp
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElse(null);
            if (user != null) {
                booking.setUser(user);
                System.out.println("DEBUG: Booking created with user_id: " + userId + ", username: " + user.getUsername());
            } else {
                System.out.println("DEBUG: User not found with userId: " + userId);
            }
        } else {
            System.out.println("DEBUG: userId is null, trying to find user by email");
            // Nếu userId null nhưng có email, thử tìm user theo email và set vào
            // (để đảm bảo booking được liên kết với user nếu user đã tồn tại)
            if (booking.getCustomerEmail() != null && !booking.getCustomerEmail().isEmpty()) {
                User userByEmail = userRepository.findByEmail(booking.getCustomerEmail())
                        .orElse(null);
                if (userByEmail != null) {
                    booking.setUser(userByEmail);
                    System.out.println("DEBUG: Booking linked to user by email: " + userByEmail.getUsername());
                } else {
                    System.out.println("DEBUG: No user found with email: " + booking.getCustomerEmail());
                }
            }
        }

        Booking savedBooking = bookingRepository.save(booking);
        
        // Reload booking để đảm bảo có đầy đủ thông tin
        savedBooking = bookingRepository.findById(savedBooking.getId()).orElse(savedBooking);

        // Gửi email xác nhận
        sendBookingConfirmationEmail(savedBooking);

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
            List<Booking> bookings = bookingRepository.findByUserId(userId);
            System.out.println("DEBUG: getBookingsByUser - userId: " + userId + ", found: " + bookings.size() + " bookings");
            return bookings;
        }
        System.out.println("DEBUG: getBookingsByUser - userId is null, returning empty list");
        return List.of(); // Trả về danh sách rỗng nếu userId null
    }
    
    public List<Booking> getBookingsByEmail(String email) {
        if (email != null && !email.isEmpty()) {
            return bookingRepository.findByCustomerEmail(email);
        }
        return List.of();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public Booking updatePaymentStatus(Long bookingId, Boolean paymentStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setPaymentStatus(paymentStatus);
        return bookingRepository.save(booking);
    }

    private void sendBookingConfirmationEmail(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getCustomerEmail());
            message.setSubject("Xác nhận đặt tour - " + booking.getTour().getName());
            message.setText(buildEmailContent(booking));
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't fail the booking
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private String buildEmailContent(Booking booking) {
        Tour tour = booking.getTour();
        StringBuilder content = new StringBuilder();
        content.append("Xin chào ").append(booking.getCustomerName()).append(",\n\n");
        content.append("Cảm ơn bạn đã đặt tour với chúng tôi!\n\n");
        content.append("THÔNG TIN ĐẶT TOUR:\n");
        content.append("========================\n");
        content.append("Mã đặt tour: #").append(booking.getId()).append("\n");
        content.append("Tên tour: ").append(tour.getName()).append("\n");
        content.append("Địa điểm: ").append(tour.getLocation()).append("\n");
        content.append("Số ngày: ").append(tour.getNumberOfDays()).append(" ngày\n");
        content.append("Ngày khởi hành: ").append(tour.getDepartureDate()).append("\n");
        content.append("Số người: ").append(booking.getNumberOfPeople()).append("\n");
        content.append("Tổng giá: ").append(booking.getTotalPrice()).append(" VNĐ\n");
        content.append("Trạng thái thanh toán: ").append(booking.getPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán").append("\n\n");
        content.append("Chúng tôi sẽ liên hệ với bạn sớm nhất có thể.\n\n");
        content.append("Trân trọng,\n");
        content.append("Công ty Du lịch");
        return content.toString();
    }
}



