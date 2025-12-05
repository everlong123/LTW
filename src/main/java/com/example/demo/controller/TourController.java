package com.example.demo.controller;

import com.example.demo.entity.Tour;
import com.example.demo.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/tours")
public class TourController {

    @Autowired
    private TourService tourService;

    // Lấy đường dẫn thư mục static/images/tours
    private Path getUploadDir() {
        String userDir = System.getProperty("user.dir");
        // Kiểm tra nếu đang chạy từ thư mục demo
        Path uploadPath = Paths.get(userDir, "src", "main", "resources", "static", "images", "tours");
        // Nếu không tồn tại, thử đường dẫn từ root project
        if (!Files.exists(uploadPath)) {
            uploadPath = Paths.get(userDir, "demo", "src", "main", "resources", "static", "images", "tours");
        }
        return uploadPath;
    }

    @GetMapping
    public String listTours(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) Integer numberOfDays,
            Model model) {
        
        List<Tour> tours;
        
        if (location != null && !location.isEmpty() || 
            minPrice != null && !minPrice.isEmpty() || 
            maxPrice != null && !maxPrice.isEmpty() || 
            numberOfDays != null) {
            
            BigDecimal min = minPrice != null && !minPrice.isEmpty() ? new BigDecimal(minPrice) : null;
            BigDecimal max = maxPrice != null && !maxPrice.isEmpty() ? new BigDecimal(maxPrice) : null;
            
            tours = tourService.searchTours(location, min, max, numberOfDays);
        } else {
            tours = tourService.getUpcomingTours();
        }
        
        model.addAttribute("tours", tours);
        model.addAttribute("location", location);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("numberOfDays", numberOfDays);
        
        return "tours/list";
    }

    @GetMapping("/{id}")
    public String viewTour(@PathVariable Long id, Model model) {
        Optional<Tour> tour = tourService.getTourById(id);
        if (tour.isPresent()) {
            model.addAttribute("tour", tour.get());
            return "tours/detail";
        }
        return "redirect:/tours";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTours(Model model) {
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "tours/admin";
    }

    @GetMapping("/admin/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newTourForm(Model model) {
        model.addAttribute("tour", new Tour());
        return "tours/form";
    }

    @GetMapping("/admin/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editTourForm(@PathVariable Long id, Model model) {
        Optional<Tour> tour = tourService.getTourById(id);
        if (tour.isPresent()) {
            model.addAttribute("tour", tour.get());
            return "tours/form";
        }
        return "redirect:/tours/admin";
    }

    @PostMapping("/admin/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveTour(
            @Valid @ModelAttribute Tour tour,
            BindingResult result,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "tours/form";
        }

        // Xử lý upload ảnh
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Lấy extension của file
                String originalFilename = imageFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                
                // Tạo tên file unique để tránh trùng lặp
                String fileName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + extension;
                
                // Lấy đường dẫn upload
                Path uploadPath = getUploadDir();
                
                // Tạo thư mục nếu chưa tồn tại
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // Lưu file
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());
                
                // Set URL cho ảnh (đường dẫn tương đối từ static)
                tour.setImageUrl("/images/tours/" + fileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh: " + e.getMessage());
                return "redirect:/tours/admin";
            }
        }

        tourService.saveTour(tour);
        redirectAttributes.addFlashAttribute("success", "Lưu tour thành công!");
        return "redirect:/tours/admin";
    }

    @PostMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTour(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        tourService.deleteTour(id);
        redirectAttributes.addFlashAttribute("success", "Xóa tour thành công!");
        return "redirect:/tours/admin";
    }
}

