package com.example.demo.controller;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Tour;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TourService tourService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/new/{tourId}")
    public String bookingForm(@PathVariable Long tourId, Model model) {
        Optional<Tour> tour = tourService.getTourById(tourId);
        if (tour.isPresent()) {
            model.addAttribute("tour", tour.get());
            model.addAttribute("booking", new Booking());
            
            // Nếu user đã đăng nhập, lấy thông tin
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                Optional<User> user = userRepository.findByUsername(auth.getName());
                if (user.isPresent()) {
                    Booking booking = new Booking();
                    booking.setCustomerName(user.get().getFullName() != null ? user.get().getFullName() : "");
                    booking.setCustomerEmail(user.get().getEmail() != null ? user.get().getEmail() : "");
                    booking.setCustomerPhone(user.get().getPhone() != null ? user.get().getPhone() : "");
                    booking.setCustomerAddress(user.get().getAddress() != null ? user.get().getAddress() : "");
                    model.addAttribute("booking", booking);
                }
            }
            
            return "bookings/form";
        }
        return "redirect:/tours";
    }

    @PostMapping("/create")
    public String createBooking(
            @Valid @ModelAttribute Booking booking,
            BindingResult result,
            @RequestParam Long tourId,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            Optional<Tour> tour = tourService.getTourById(tourId);
            if (tour.isPresent()) {
                redirectAttributes.addFlashAttribute("tour", tour.get());
                return "redirect:/bookings/new/" + tourId;
            }
            return "redirect:/tours";
        }

        try {
            Long userId = null;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                Optional<User> user = userRepository.findByUsername(auth.getName());
                if (user.isPresent()) {
                    userId = user.get().getId();
                }
            }

            Booking savedBooking = bookingService.createBooking(booking, tourId, userId);
            redirectAttributes.addFlashAttribute("success", "Đặt tour thành công! Email xác nhận đã được gửi đến " + booking.getCustomerEmail());
            return "redirect:/bookings/" + savedBooking.getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings/new/" + tourId;
        }
    }

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "bookings/detail";
        }
        return "redirect:/tours";
    }

    @GetMapping("/my-bookings")
    public String myBookings(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Optional<User> user = userRepository.findByUsername(auth.getName());
            if (user.isPresent()) {
                List<Booking> bookings = bookingService.getBookingsByUser(user.get().getId());
                model.addAttribute("bookings", bookings);
                return "bookings/my-bookings";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookings/admin";
    }

    @GetMapping("/admin/tour/{tourId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String bookingsByTour(@PathVariable Long tourId, Model model) {
        List<Booking> bookings = bookingService.getBookingsByTour(tourId);
        Optional<Tour> tour = tourService.getTourById(tourId);
        if (tour.isPresent()) {
            model.addAttribute("tour", tour.get());
        }
        model.addAttribute("bookings", bookings);
        return "bookings/by-tour";
    }

    @PostMapping("/admin/update-payment/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam Boolean paymentStatus,
            RedirectAttributes redirectAttributes) {
        bookingService.updatePaymentStatus(id, paymentStatus);
        redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thanh toán thành công!");
        return "redirect:/bookings/admin";
    }
}



