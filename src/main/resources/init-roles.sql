-- SQL Script để tạo Roles và Admin user (chạy thủ công nếu cần)
-- Chạy script này trong MySQL nếu muốn có roles và admin user

-- Tạo Roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO roles (name) VALUES ('ROLE_USER') ON DUPLICATE KEY UPDATE name=name;

-- Tạo Admin user (password: admin123 - đã mã hóa BCrypt)
-- Lưu ý: Cần thay đổi password hash nếu muốn dùng mật khẩu khác
INSERT INTO users (username, password, email, full_name) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwy8pK0O', 'admin@example.com', 'Administrator')
ON DUPLICATE KEY UPDATE username=username;

-- Gán ROLE_ADMIN cho admin user
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE user_id=user_id;



