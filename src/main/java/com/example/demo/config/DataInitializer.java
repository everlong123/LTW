package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer - Tạo dữ liệu cơ bản cho hệ thống
 * Tự động tạo Roles, Admin user và các user mẫu khi ứng dụng khởi động
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo ROLE_ADMIN nếu chưa có
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role role = new Role("ROLE_ADMIN");
                    return roleRepository.save(role);
                });

        // Tạo ROLE_USER nếu chưa có (cần cho UserService khi đăng ký)
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role("ROLE_USER");
                    return roleRepository.save(role);
                });

        // Tạo hoặc cập nhật tài khoản admin mặc định
        User admin = userRepository.findByUsername("admin").orElse(new User());
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setEmail("admin@example.com");
        admin.setFullName("Administrator");
        if (admin.getRoles().isEmpty()) {
            admin.addRole(adminRole);
        }
        userRepository.save(admin);

        // Tạo hoặc cập nhật các tài khoản user mẫu
        createSampleUser("user1", "123", "user1@example.com", "Nguyễn Văn A", "0123456789", "123 Đường ABC, Quận 1, TP.HCM", userRole);
        createSampleUser("user2", "123", "user2@example.com", "Trần Thị B", "0987654321", "456 Đường XYZ, Quận 2, TP.HCM", userRole);
        createSampleUser("user3", "123", "user3@example.com", "Lê Văn C", "0912345678", "789 Đường DEF, Quận 3, TP.HCM", userRole);
    }

    private void createSampleUser(String username, String password, String email, String fullName, String phone, String address, Role role) {
        // Tạo hoặc cập nhật user (cập nhật mật khẩu nếu user đã tồn tại)
        User user = userRepository.findByUsername(username).orElse(new User());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Luôn cập nhật mật khẩu
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
        if (user.getRoles().isEmpty()) {
            user.addRole(role);
        }
        userRepository.save(user);
    }
}

