-- SQL Script để Backup/Export dữ liệu từ database
-- Chạy script này để lưu dữ liệu hiện tại ra file SQL
-- Có thể dùng để restore sau này

-- ============================================
-- BACKUP ROLES
-- ============================================
-- Export roles
SELECT CONCAT('INSERT INTO roles (id, name) VALUES (', id, ', ''', name, ''') ON DUPLICATE KEY UPDATE name=name;') 
AS '-- Roles'
FROM roles;

-- ============================================
-- BACKUP USERS
-- ============================================
-- Export users (không export password vì đã mã hóa)
SELECT CONCAT('INSERT INTO users (id, username, password, email, full_name, phone, address) VALUES (',
    id, ', ''', username, ''', ''', password, ''', ''', 
    COALESCE(email, ''), ''', ''', COALESCE(full_name, ''), ''', ''', 
    COALESCE(phone, ''), ''', ''', COALESCE(address, ''), ''') ON DUPLICATE KEY UPDATE username=username;') 
AS '-- Users'
FROM users;

-- ============================================
-- BACKUP USER_ROLES (Quan hệ Many-to-Many)
-- ============================================
SELECT CONCAT('INSERT INTO user_roles (user_id, role_id) VALUES (', user_id, ', ', role_id, ') ON DUPLICATE KEY UPDATE user_id=user_id;') 
AS '-- User Roles'
FROM user_roles;

-- ============================================
-- BACKUP TOURS (nếu có)
-- ============================================
SELECT CONCAT('INSERT INTO tours (id, name, image_url, price, location, number_of_days, departure_date, max_customers, description) VALUES (',
    id, ', ''', REPLACE(name, '''', ''''''), ''', ''', 
    COALESCE(image_url, ''), ''', ', price, ', ''', 
    REPLACE(location, '''', ''''''), ''', ', number_of_days, ', ''', 
    departure_date, ''', ', max_customers, ', ''', 
    REPLACE(COALESCE(description, ''), '''', ''''''), ''') ON DUPLICATE KEY UPDATE name=name;') 
AS '-- Tours'
FROM tours;

-- ============================================
-- BACKUP BOOKINGS (nếu có)
-- ============================================
SELECT CONCAT('INSERT INTO bookings (id, tour_id, user_id, customer_name, customer_email, customer_phone, customer_address, number_of_people, total_price, payment_status, booking_date) VALUES (',
    id, ', ', tour_id, ', ', 
    COALESCE(user_id, 'NULL'), ', ''', 
    REPLACE(customer_name, '''', ''''''), ''', ''', 
    customer_email, ''', ''', customer_phone, ''', ''', 
    REPLACE(COALESCE(customer_address, ''), '''', ''''''), ''', ', 
    number_of_people, ', ', total_price, ', ', 
    payment_status, ', ''', booking_date, ''') ON DUPLICATE KEY UPDATE id=id;') 
AS '-- Bookings'
FROM bookings;

-- ============================================
-- CÁCH SỬ DỤNG:
-- ============================================
-- 1. Chạy các câu SELECT trên trong MySQL
-- 2. Copy kết quả và lưu vào file .sql
-- 3. Hoặc dùng mysqldump:
--    mysqldump -u root -p tour_db > backup.sql
--
-- RESTORE:
--    mysql -u root -p tour_db < backup.sql
-- ============================================



