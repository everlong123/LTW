# Hệ Thống Quản Lý Tour Du Lịch

## Mô tả
Hệ thống quản lý tour du lịch cho phép công ty du lịch quản lý tour và khách hàng đặt tour online. Hệ thống hỗ trợ đầy đủ các chức năng quản lý tour, đặt tour, và gửi email xác nhận.

## Tính năng chính

### Dành cho Admin:
- ✅ Đăng tour mới, chỉnh sửa thông tin tour
- ✅ Theo dõi khách đặt tour
- ✅ Kiểm soát số lượng khách tối đa cho mỗi tour
- ✅ Quản lý lịch khởi hành
- ✅ Xem danh sách khách theo từng tour
- ✅ Cập nhật trạng thái thanh toán

### Dành cho Khách hàng:
- ✅ Xem danh mục tour
- ✅ Lọc tour theo giá, địa điểm, số ngày
- ✅ Đặt tour online
- ✅ Nhận email xác nhận đặt tour
- ✅ Xem lịch sử đặt tour của mình

## Cấu trúc dự án:

```
demo/
├── src/main/java/com/example/demo/
│   ├── entity/
│   │   ├── User.java          # Entity User với thông tin khách hàng
│   │   ├── Role.java          # Entity Role
│   │   ├── Tour.java          # Entity Tour (tên, ảnh, giá, địa điểm, số ngày, ngày khởi hành, max khách, mô tả)
│   │   └── Booking.java       # Entity Booking (thông tin đặt tour, số người, tổng giá, trạng thái thanh toán)
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── RoleRepository.java
│   │   ├── TourRepository.java
│   │   └── BookingRepository.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── TourService.java
│   │   └── BookingService.java  # Bao gồm gửi email xác nhận
│   ├── security/
│   │   ├── CustomUserDetails.java
│   │   └── CustomUserDetailsService.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── DataInitializer.java  # Khởi tạo ROLE_ADMIN và tài khoản admin
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── HomeController.java
│   │   ├── TourController.java   # CRUD tour, danh sách tour, lọc tour
│   │   └── BookingController.java # Đặt tour, quản lý booking
│   └── dto/
│       └── RegisterRequest.java
└── src/main/resources/
    ├── templates/
    │   ├── home.html
    │   ├── login.html
    │   ├── register.html
    │   ├── tours/
    │   │   ├── list.html        # Danh sách tour với bộ lọc
    │   │   ├── detail.html     # Chi tiết tour
    │   │   ├── form.html        # Form thêm/sửa tour (admin)
    │   │   └── admin.html       # Quản lý tour (admin)
    │   └── bookings/
    │       ├── form.html        # Form đặt tour
    │       ├── detail.html     # Xác nhận đặt tour
    │       ├── my-bookings.html # Tour của tôi
    │       ├── admin.html       # Quản lý đặt tour (admin)
    │       └── by-tour.html    # Danh sách khách theo tour (admin)
    └── static/css/
        └── style.css
```

## Cách chạy:

1. **Cấu hình MySQL trong `application.properties`:**
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

2. **Cấu hình Email (Gmail SMTP) trong `application.properties`:**
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```
   *Lưu ý: Cần tạo App Password từ Google Account để sử dụng*

3. **Chạy ứng dụng:**
   ```bash
   cd demo
   .\gradlew.bat bootRun
   ```

4. **Truy cập:**
   - Trang chủ/Tour: `http://localhost:8080/tours`
   - Đăng ký: `http://localhost:8080/register`
   - Đăng nhập: `http://localhost:8080/login`
   - Admin Tour: `http://localhost:8080/tours/admin` (cần đăng nhập với ROLE_ADMIN)
   - Admin Booking: `http://localhost:8080/bookings/admin` (cần đăng nhập với ROLE_ADMIN)

## Tài khoản mặc định:

- **Admin:**
  - Username: `admin`
  - Password: `admin123`
  - Role: `ROLE_ADMIN`

## Database Schema:

### Bảng `tours`:
- `id` - ID tour
- `name` - Tên tour
- `image_url` - URL ảnh tour
- `price` - Giá tour (VNĐ)
- `location` - Địa điểm
- `number_of_days` - Số ngày
- `departure_date` - Ngày khởi hành
- `max_customers` - Số khách tối đa
- `description` - Mô tả chi tiết

### Bảng `bookings`:
- `id` - ID đặt tour
- `tour_id` - ID tour (FK)
- `user_id` - ID user (FK, nullable)
- `customer_name` - Họ tên khách hàng
- `customer_email` - Email khách hàng
- `customer_phone` - Số điện thoại
- `customer_address` - Địa chỉ
- `number_of_people` - Số người đi
- `total_price` - Tổng giá
- `payment_status` - Trạng thái thanh toán (boolean)
- `booking_date` - Ngày đặt tour

### Bảng `users`:
- `id` - ID user
- `username` - Tên đăng nhập
- `password` - Mật khẩu (đã mã hóa)
- `email` - Email
- `full_name` - Họ tên
- `phone` - Số điện thoại
- `address` - Địa chỉ

### Bảng `roles`:
- `id` - ID role
- `name` - Tên role (ROLE_USER, ROLE_ADMIN)

### Bảng `user_roles`:
- `user_id` - ID user (FK)
- `role_id` - ID role (FK)

## Tính năng chi tiết:

### Quản lý Tour (Admin):
1. **Thêm tour mới:**
   - Upload ảnh tour
   - Nhập đầy đủ thông tin: tên, giá, địa điểm, số ngày, ngày khởi hành, số khách tối đa, mô tả
   
2. **Chỉnh sửa tour:**
   - Cập nhật thông tin tour
   - Thay đổi ảnh tour
   
3. **Xóa tour:**
   - Xóa tour (có xác nhận)
   
4. **Xem danh sách khách đặt tour:**
   - Xem tất cả khách đặt tour
   - Xem khách đặt theo từng tour
   - Cập nhật trạng thái thanh toán

### Đặt Tour (Khách hàng):
1. **Xem danh sách tour:**
   - Hiển thị tất cả tour sắp khởi hành
   - Lọc theo: địa điểm, giá (min-max), số ngày
   
2. **Xem chi tiết tour:**
   - Thông tin đầy đủ về tour
   - Số chỗ còn lại
   - Nút đặt tour
   
3. **Đặt tour:**
   - Nhập thông tin cá nhân
   - Chọn số lượng người đi
   - Xem tổng giá tự động tính
   - Xác nhận đặt tour
   - Nhận email xác nhận tự động

4. **Xem tour đã đặt:**
   - Danh sách tour đã đặt
   - Trạng thái thanh toán
   - Chi tiết từng đặt tour

## Security:

- **Public access:** `/tours`, `/tours/**`, `/bookings/new/**`, `/bookings/create`, `/bookings/*`
- **Authenticated:** `/bookings/my-bookings`
- **Admin only:** `/tours/admin/**`, `/bookings/admin/**`

## Email Configuration:

Hệ thống tự động gửi email xác nhận khi khách hàng đặt tour thành công. Email bao gồm:
- Mã đặt tour
- Thông tin tour
- Thông tin khách hàng
- Số người, tổng giá
- Trạng thái thanh toán

## Công nghệ sử dụng:

- **Backend:** Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **Database:** MySQL
- **Frontend:** Thymeleaf, HTML, CSS
- **Email:** Spring Mail (Gmail SMTP)
- **Build Tool:** Gradle
- **Java:** 17
