-- SQL Script để import dữ liệu mẫu
-- Chạy script này nếu muốn import nhanh dữ liệu mà không cần compile Java
-- Lưu ý: Cần tắt DataInitializer hoặc đảm bảo không trùng lặp dữ liệu

-- Xóa dữ liệu cũ (nếu cần)
-- DELETE FROM bookings;
-- DELETE FROM tours;
-- DELETE FROM user_roles;
-- DELETE FROM users;
-- DELETE FROM roles;

-- Tạo Roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO roles (name) VALUES ('ROLE_USER') ON DUPLICATE KEY UPDATE name=name;

-- Tạo Users (password đã được mã hóa BCrypt: admin123, user123)
-- Password: admin123
INSERT INTO users (username, password, email, full_name, phone, address) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwy8pK0O', 'admin@example.com', 'Administrator', '0123456789', 'Hà Nội')
ON DUPLICATE KEY UPDATE username=username;

-- Password: user123
INSERT INTO users (username, password, email, full_name, phone, address) 
VALUES ('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwy8pK0O', 'user1@example.com', 'Nguyễn Văn A', '0987654321', 'Hồ Chí Minh')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO users (username, password, email, full_name, phone, address) 
VALUES ('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwy8pK0O', 'user2@example.com', 'Trần Thị B', '0912345678', 'Đà Nẵng')
ON DUPLICATE KEY UPDATE username=username;

-- Gán roles cho users
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE user_id=user_id;

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'user1' AND r.name = 'ROLE_USER'
ON DUPLICATE KEY UPDATE user_id=user_id;

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'user2' AND r.name = 'ROLE_USER'
ON DUPLICATE KEY UPDATE user_id=user_id;

-- Tạo Tours
INSERT INTO tours (name, price, location, number_of_days, departure_date, max_customers, description) VALUES
('Tour Hà Nội - Sapa 3 ngày 2 đêm', 3500000, 'Hà Nội - Sapa', 3, DATE_ADD(CURDATE(), INTERVAL 15 DAY), 30, 
'Khám phá vẻ đẹp của Sapa với những thửa ruộng bậc thang tuyệt đẹp, thăm các bản làng dân tộc thiểu số, trải nghiệm văn hóa địa phương độc đáo. Tour bao gồm: xe đưa đón, khách sạn 3 sao, hướng dẫn viên, bữa ăn theo chương trình.'),

('Tour Hạ Long Bay - 2 ngày 1 đêm', 2500000, 'Hạ Long', 2, DATE_ADD(CURDATE(), INTERVAL 20 DAY), 25,
'Trải nghiệm vịnh Hạ Long - Di sản thiên nhiên thế giới. Du thuyền trên vịnh, thăm hang động, tắm biển, chèo kayak. Bao gồm: du thuyền, phòng nghỉ, bữa ăn hải sản tươi ngon.'),

('Tour Huế - Đà Nẵng - Hội An 4 ngày 3 đêm', 4500000, 'Huế - Đà Nẵng - Hội An', 4, DATE_ADD(CURDATE(), INTERVAL 25 DAY), 20,
'Hành trình khám phá miền Trung: Thăm cố đô Huế với Đại Nội, lăng tẩm các vua, Đà Nẵng với Bà Nà Hills, Hội An cổ kính. Tour bao gồm: xe du lịch, khách sạn 4 sao, vé tham quan, hướng dẫn viên.'),

('Tour Phú Quốc - 3 ngày 2 đêm', 3200000, 'Phú Quốc', 3, DATE_ADD(CURDATE(), INTERVAL 30 DAY), 35,
'Nghỉ dưỡng tại đảo ngọc Phú Quốc. Tắm biển, lặn ngắm san hô, thăm làng chài, thưởng thức hải sản tươi sống. Bao gồm: vé máy bay, khách sạn resort, bữa ăn, tour lặn biển.'),

('Tour Nha Trang - 3 ngày 2 đêm', 2800000, 'Nha Trang', 3, DATE_ADD(CURDATE(), INTERVAL 18 DAY), 28,
'Thành phố biển Nha Trang xinh đẹp. Tham quan Vinpearl, đảo Hòn Mun, tắm bùn Tháp Bà, thưởng thức hải sản. Bao gồm: khách sạn 3-4 sao, vé tham quan, bữa ăn.'),

('Tour Đà Lạt - 2 ngày 1 đêm', 1800000, 'Đà Lạt', 2, DATE_ADD(CURDATE(), INTERVAL 12 DAY), 22,
'Thành phố ngàn hoa Đà Lạt. Thăm thác Datanla, vườn hoa, đồi chè Cầu Đất, thưởng thức đặc sản địa phương. Bao gồm: xe đưa đón, khách sạn, bữa ăn.'),

('Tour Mù Cang Chải - Mùa vàng 3 ngày 2 đêm', 2900000, 'Mù Cang Chải', 3, DATE_ADD(CURDATE(), INTERVAL 40 DAY), 15,
'Ngắm ruộng bậc thang vàng óng tại Mù Cang Chải vào mùa lúa chín. Chụp ảnh, trải nghiệm văn hóa dân tộc Mông, thưởng thức ẩm thực địa phương. Bao gồm: xe du lịch, homestay, hướng dẫn viên.'),

('Tour Miền Tây - Cần Thơ 2 ngày 1 đêm', 1500000, 'Cần Thơ', 2, DATE_ADD(CURDATE(), INTERVAL 22 DAY), 30,
'Khám phá miền Tây sông nước. Đi chợ nổi Cái Răng, vườn trái cây, nghe đờn ca tài tử, thưởng thức đặc sản miền Tây. Bao gồm: xe đưa đón, khách sạn, bữa ăn đặc sản.');

-- Tạo Bookings (cần lấy tour_id và user_id từ bảng trên)
INSERT INTO bookings (tour_id, user_id, customer_name, customer_email, customer_phone, customer_address, number_of_people, total_price, payment_status, booking_date)
SELECT 
    (SELECT id FROM tours WHERE name = 'Tour Hà Nội - Sapa 3 ngày 2 đêm' LIMIT 1),
    (SELECT id FROM users WHERE username = 'user1' LIMIT 1),
    'Nguyễn Văn A',
    'user1@example.com',
    '0987654321',
    'Hồ Chí Minh',
    2,
    7000000,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 2 DAY)
WHERE NOT EXISTS (SELECT 1 FROM bookings WHERE customer_email = 'user1@example.com' AND tour_id = (SELECT id FROM tours WHERE name = 'Tour Hà Nội - Sapa 3 ngày 2 đêm' LIMIT 1));

INSERT INTO bookings (tour_id, user_id, customer_name, customer_email, customer_phone, customer_address, number_of_people, total_price, payment_status, booking_date)
SELECT 
    (SELECT id FROM tours WHERE name = 'Tour Hạ Long Bay - 2 ngày 1 đêm' LIMIT 1),
    (SELECT id FROM users WHERE username = 'user2' LIMIT 1),
    'Trần Thị B',
    'user2@example.com',
    '0912345678',
    'Đà Nẵng',
    3,
    7500000,
    FALSE,
    DATE_SUB(NOW(), INTERVAL 1 DAY)
WHERE NOT EXISTS (SELECT 1 FROM bookings WHERE customer_email = 'user2@example.com' AND tour_id = (SELECT id FROM tours WHERE name = 'Tour Hạ Long Bay - 2 ngày 1 đêm' LIMIT 1));

INSERT INTO bookings (tour_id, user_id, customer_name, customer_email, customer_phone, customer_address, number_of_people, total_price, payment_status, booking_date)
SELECT 
    (SELECT id FROM tours WHERE name = 'Tour Huế - Đà Nẵng - Hội An 4 ngày 3 đêm' LIMIT 1),
    NULL,
    'Lê Văn C',
    'levanc@example.com',
    '0901234567',
    'Hải Phòng',
    4,
    18000000,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 5 HOUR)
WHERE NOT EXISTS (SELECT 1 FROM bookings WHERE customer_email = 'levanc@example.com' AND tour_id = (SELECT id FROM tours WHERE name = 'Tour Huế - Đà Nẵng - Hội An 4 ngày 3 đêm' LIMIT 1));



