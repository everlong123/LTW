-- ============================================
-- EXPORT DỮ LIỆU - Script đơn giản để export
-- ============================================
-- Chạy script này để xem dữ liệu hiện có
-- Copy kết quả và lưu vào file backup

-- Xem tất cả Roles
SELECT * FROM roles;

-- Xem tất cả Users (không hiển thị password)
SELECT id, username, email, full_name, phone, address FROM users;

-- Xem User và Roles của họ
SELECT 
    u.id AS user_id,
    u.username,
    r.name AS role_name
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
ORDER BY u.id;

-- Xem Tours (nếu có)
SELECT * FROM tours;

-- Xem Bookings (nếu có)
SELECT 
    b.id,
    b.customer_name,
    b.customer_email,
    t.name AS tour_name,
    b.number_of_people,
    b.total_price,
    b.payment_status,
    b.booking_date
FROM bookings b
LEFT JOIN tours t ON b.tour_id = t.id
ORDER BY b.booking_date DESC;

-- ============================================
-- SỬ DỤNG MYSQLDUMP (Khuyến nghị)
-- ============================================
-- Export toàn bộ database:
-- mysqldump -u root -p tour_db > backup-full.sql
--
-- Export chỉ dữ liệu (không có structure):
-- mysqldump -u root -p --no-create-info tour_db > backup-data-only.sql
--
-- Export chỉ structure (không có data):
-- mysqldump -u root -p --no-data tour_db > backup-structure-only.sql
--
-- Export một số bảng cụ thể:
-- mysqldump -u root -p tour_db roles users user_roles > backup-users.sql
-- ============================================



