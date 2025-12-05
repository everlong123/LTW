# Hệ Thống Quản Lý Tour Du Lịch

## Tổng Quan Dự Án

Đây là một hệ thống quản lý tour du lịch được xây dựng bằng Spring Boot, cho phép công ty du lịch quản lý các tour và khách hàng đặt tour trực tuyến. Hệ thống được thiết kế theo kiến trúc MVC (Model-View-Controller) với Spring Security để bảo mật và phân quyền người dùng.

## Kiến Trúc Hệ Thống

### Mô Hình Kiến Trúc

Hệ thống được xây dựng theo mô hình 3 tầng (Three-Tier Architecture):

1. **Presentation Layer (Tầng Giao Diện)**: Thymeleaf templates, HTML, CSS
2. **Business Logic Layer (Tầng Logic Nghiệp Vụ)**: Controllers và Services
3. **Data Access Layer (Tầng Truy Cập Dữ Liệu)**: Repositories và Entities

### Công Nghệ Sử Dụng

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Build Tool**: Gradle
- **Database**: MySQL 8.0+
- **ORM**: Spring Data JPA với Hibernate
- **Security**: Spring Security 6
- **Template Engine**: Thymeleaf
- **Email**: Spring Mail (Gmail SMTP)
- **Validation**: Jakarta Bean Validation

## Cấu Trúc Thư Mục Dự Án

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/              # Cấu hình hệ thống
│   │   │   │   ├── DataInitializer.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/          # Xử lý HTTP requests
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   └── TourController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   └── RegisterRequest.java
│   │   │   ├── entity/              # JPA Entities (Database tables)
│   │   │   │   ├── Booking.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Tour.java
│   │   │   │   └── User.java
│   │   │   ├── repository/          # Data Access Layer
│   │   │   │   ├── BookingRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   ├── TourRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/            # Spring Security components
│   │   │   │   ├── CustomUserDetails.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   ├── service/             # Business Logic Layer
│   │   │   │   ├── BookingService.java
│   │   │   │   ├── TourService.java
│   │   │   │   └── UserService.java
│   │   │   └── DemoApplication.java # Entry point
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   └── css/
│   │       │       └── style.css
│   │       └── templates/
│   │           ├── bookings/
│   │           ├── tours/
│   │           ├── home.html
│   │           ├── login.html
│   │           └── register.html
│   └── test/                        # Unit tests
├── build.gradle                      # Dependencies và build configuration
└── README.md                         # Tài liệu này
```

## Cơ Sở Dữ Liệu

### Sơ Đồ Quan Hệ (ERD)

Hệ thống sử dụng 5 bảng chính với các quan hệ như sau:

```
users (1) ----< (N) user_roles (N) >---- (1) roles
users (1) ----< (N) bookings
tours (1) ----< (N) bookings
```

### Mô Tả Các Bảng

#### 1. Bảng `users`
Lưu trữ thông tin người dùng trong hệ thống.

| Cột | Kiểu Dữ Liệu | Mô Tả |
|-----|--------------|-------|
| id | BIGINT (PK, AUTO_INCREMENT) | Khóa chính, tự động tăng |
| username | VARCHAR(50), UNIQUE, NOT NULL | Tên đăng nhập, duy nhất |
| password | VARCHAR(255), NOT NULL | Mật khẩu đã mã hóa BCrypt |
| email | VARCHAR(255) | Email người dùng |
| full_name | VARCHAR(255) | Họ và tên đầy đủ |
| phone | VARCHAR(20) | Số điện thoại |
| address | VARCHAR(500) | Địa chỉ |

**Quan hệ**: 
- Many-to-Many với `roles` qua bảng trung gian `user_roles`
- One-to-Many với `bookings` (một user có thể có nhiều booking)

#### 2. Bảng `roles`
Lưu trữ các vai trò trong hệ thống (ROLE_ADMIN, ROLE_USER).

| Cột | Kiểu Dữ Liệu | Mô Tả |
|-----|--------------|-------|
| id | BIGINT (PK, AUTO_INCREMENT) | Khóa chính |
| name | VARCHAR(50), UNIQUE, NOT NULL | Tên role (ROLE_ADMIN, ROLE_USER) |

**Quan hệ**: Many-to-Many với `users` qua bảng trung gian `user_roles`

#### 3. Bảng `user_roles`
Bảng trung gian để liên kết users và roles (quan hệ Many-to-Many).

| Cột | Kiểu Dữ Liệu | Mô Tả |
|-----|--------------|-------|
| user_id | BIGINT (FK) | Khóa ngoại đến users.id |
| role_id | BIGINT (FK) | Khóa ngoại đến roles.id |

#### 4. Bảng `tours`
Lưu trữ thông tin các tour du lịch.

| Cột | Kiểu Dữ Liệu | Mô Tả |
|-----|--------------|-------|
| id | BIGINT (PK, AUTO_INCREMENT) | Khóa chính |
| name | VARCHAR(200), NOT NULL | Tên tour |
| image_url | VARCHAR(500) | Đường dẫn ảnh tour |
| price | DECIMAL(15,2), NOT NULL | Giá tour (VNĐ) |
| location | VARCHAR(100), NOT NULL | Địa điểm tour |
| number_of_days | INT, NOT NULL | Số ngày tour |
| departure_date | DATE, NOT NULL | Ngày khởi hành |
| max_customers | INT, NOT NULL | Số khách tối đa |
| description | TEXT | Mô tả chi tiết tour |

**Quan hệ**: One-to-Many với `bookings` (một tour có thể có nhiều booking)

#### 5. Bảng `bookings`
Lưu trữ thông tin đặt tour của khách hàng.

| Cột | Kiểu Dữ Liệu | Mô Tả |
|-----|--------------|-------|
| id | BIGINT (PK, AUTO_INCREMENT) | Khóa chính |
| tour_id | BIGINT (FK), NOT NULL | Khóa ngoại đến tours.id |
| user_id | BIGINT (FK), NULL | Khóa ngoại đến users.id (nullable - khách vãng lai) |
| customer_name | VARCHAR(100), NOT NULL | Họ tên khách hàng |
| customer_email | VARCHAR(100), NOT NULL | Email khách hàng |
| customer_phone | VARCHAR(20), NOT NULL | Số điện thoại |
| customer_address | VARCHAR(500) | Địa chỉ khách hàng |
| number_of_people | INT, NOT NULL | Số người đi tour |
| total_price | DECIMAL(15,2), NOT NULL | Tổng giá (price × number_of_people) |
| payment_status | BOOLEAN, NOT NULL, DEFAULT FALSE | Trạng thái thanh toán |
| booking_date | DATETIME, NOT NULL | Ngày đặt tour (tự động) |

**Quan hệ**: 
- Many-to-One với `tours` (nhiều booking thuộc một tour)
- Many-to-One với `users` (nhiều booking thuộc một user, có thể NULL)

### Tự Động Tạo Schema

Hệ thống sử dụng Hibernate với `spring.jpa.hibernate.ddl-auto=update` để tự động tạo và cập nhật schema database khi ứng dụng khởi động. Điều này có nghĩa là:

- Lần đầu chạy: Hibernate sẽ tạo tất cả các bảng dựa trên Entity classes
- Các lần chạy sau: Hibernate sẽ so sánh Entity với database và tự động thêm/cập nhật các cột mới (không xóa dữ liệu cũ)

## Giải Thích Chi Tiết Các Component

### 1. Entity Layer (Tầng Thực Thể)

Entities là các class Java đại diện cho các bảng trong database. Chúng sử dụng JPA annotations để ánh xạ Java objects với database tables.

#### User.java
**Vai trò**: Đại diện cho bảng `users`, lưu trữ thông tin người dùng.

**Các annotation quan trọng**:
- `@Entity`: Đánh dấu class là một JPA entity
- `@Table(name = "users")`: Chỉ định tên bảng trong database
- `@Id`: Đánh dấu trường là khóa chính
- `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Tự động tăng ID
- `@ManyToMany`: Quan hệ nhiều-nhiều với Role
- `@JoinTable`: Cấu hình bảng trung gian `user_roles`

**Validation**:
- `@NotBlank`: Không được để trống
- `@Size(min = 3, max = 50)`: Độ dài username từ 3-50 ký tự
- `@Size(min = 6)`: Mật khẩu tối thiểu 6 ký tự

#### Role.java
**Vai trò**: Đại diện cho bảng `roles`, quản lý các vai trò trong hệ thống.

**Quan hệ**: Many-to-Many với User (một user có thể có nhiều roles, một role có thể gán cho nhiều users)

#### Tour.java
**Vai trò**: Đại diện cho bảng `tours`, lưu trữ thông tin tour du lịch.

**Các method đặc biệt**:
- `getCurrentBookings()`: Tính tổng số khách đã đặt tour
- `getAvailableSlots()`: Tính số chỗ còn lại
- `isAvailable()`: Kiểm tra tour còn khả dụng không

**Quan hệ**: One-to-Many với Booking (một tour có nhiều booking)

#### Booking.java
**Vai trò**: Đại diện cho bảng `bookings`, lưu trữ thông tin đặt tour.

**Annotation đặc biệt**:
- `@PrePersist`: Tự động set `bookingDate` trước khi lưu vào database
- `@ManyToOne`: Quan hệ nhiều-một với Tour và User

### 2. Repository Layer (Tầng Truy Cập Dữ Liệu)

Repositories là interface kế thừa từ `JpaRepository`, cung cấp các method CRUD cơ bản và có thể định nghĩa custom queries.

#### UserRepository.java
**Vai trò**: Truy cập dữ liệu bảng `users`.

**Methods**:
- `findByUsername(String username)`: Tìm user theo username
- `existsByUsername(String username)`: Kiểm tra username đã tồn tại chưa

#### TourRepository.java
**Vai trò**: Truy cập dữ liệu bảng `tours`.

**Custom Query**:
- `@Query`: Định nghĩa query tùy chỉnh để tìm tour theo location, price range, số ngày

#### BookingRepository.java
**Vai trò**: Truy cập dữ liệu bảng `bookings`.

**Methods**:
- `findByTourId(Long tourId)`: Lấy tất cả booking của một tour
- `findByUserId(Long userId)`: Lấy tất cả booking của một user

### 3. Service Layer (Tầng Logic Nghiệp Vụ)

Services chứa business logic, xử lý các nghiệp vụ phức tạp và gọi repositories để truy cập database.

#### UserService.java
**Vai trò**: Xử lý logic liên quan đến User.

**Method chính**:
- `registerUser(User user)`: Đăng ký user mới
  - Kiểm tra username đã tồn tại chưa
  - Mã hóa mật khẩu bằng BCrypt
  - Gán role mặc định ROLE_USER
  - Lưu vào database

**Dependency Injection**: Sử dụng `@Autowired` để inject `PasswordEncoder` từ SecurityConfig

#### TourService.java
**Vai trò**: Xử lý logic liên quan đến Tour.

**Methods**:
- `getAllTours()`: Lấy tất cả tours
- `getTourById(Long id)`: Lấy tour theo ID
- `saveTour(Tour tour)`: Lưu tour (create hoặc update)
- `deleteTour(Long id)`: Xóa tour
- `searchTours(...)`: Tìm tour theo các tiêu chí
- `getUpcomingTours()`: Lấy các tour sắp khởi hành

#### BookingService.java
**Vai trò**: Xử lý logic liên quan đến Booking.

**Method chính**:
- `createBooking(Booking booking, Long tourId, Long userId)`: Tạo booking mới
  - Kiểm tra số chỗ còn lại
  - Tính tổng giá (price × number_of_people)
  - Lưu booking
  - Gửi email xác nhận tự động

**Email Integration**: Sử dụng `JavaMailSender` để gửi email xác nhận đặt tour

### 4. Controller Layer (Tầng Điều Khiển)

Controllers xử lý HTTP requests, nhận dữ liệu từ client, gọi services, và trả về view hoặc redirect.

#### AuthController.java
**Vai trò**: Xử lý authentication (đăng ký, đăng nhập).

**Endpoints**:
- `GET /login`: Hiển thị form đăng nhập
- `GET /register`: Hiển thị form đăng ký
- `POST /register-process`: Xử lý đăng ký
  - Validate dữ liệu với `@Valid`
  - Gọi `UserService.registerUser()`
  - Redirect về login nếu thành công

**Validation**: Sử dụng `@Valid` và `BindingResult` để validate form data

#### TourController.java
**Vai trò**: Xử lý các request liên quan đến Tour.

**Endpoints Public**:
- `GET /tours`: Danh sách tours với filter (location, price, số ngày)
- `GET /tours/{id}`: Chi tiết tour

**Endpoints Admin** (yêu cầu ROLE_ADMIN):
- `GET /tours/admin`: Quản lý tours (CRUD)
- `GET /tours/admin/new`: Form thêm tour mới
- `GET /tours/admin/edit/{id}`: Form sửa tour
- `POST /tours/admin/save`: Lưu tour (create/update)
- `POST /tours/admin/delete/{id}`: Xóa tour

**File Upload**: Xử lý upload ảnh tour, lưu vào `static/images/tours/`

**Security**: Sử dụng `@PreAuthorize("hasRole('ADMIN')")` để bảo vệ admin endpoints

#### BookingController.java
**Vai trò**: Xử lý các request liên quan đến Booking.

**Endpoints Public**:
- `GET /bookings/new/{tourId}`: Form đặt tour
- `POST /bookings/create`: Tạo booking mới
- `GET /bookings/{id}`: Chi tiết booking

**Endpoints Authenticated**:
- `GET /bookings/my-bookings`: Danh sách booking của user hiện tại

**Endpoints Admin**:
- `GET /bookings/admin`: Quản lý tất cả bookings
- `GET /bookings/admin/tour/{tourId}`: Danh sách booking theo tour
- `POST /bookings/admin/update-payment/{id}`: Cập nhật trạng thái thanh toán

**Authentication Context**: Sử dụng `SecurityContextHolder` để lấy thông tin user hiện tại

#### HomeController.java
**Vai trò**: Xử lý trang chủ.

**Endpoints**:
- `GET /`: Trang chủ
- `GET /home`: Trang chủ (alias)

### 5. Security Layer (Tầng Bảo Mật)

#### SecurityConfig.java
**Vai trò**: Cấu hình Spring Security cho toàn bộ ứng dụng.

**Các Bean quan trọng**:

1. **PasswordEncoder Bean**:
   - Sử dụng BCryptPasswordEncoder để mã hóa mật khẩu
   - BCrypt là thuật toán one-way hashing, không thể giải mã ngược

2. **DaoAuthenticationProvider Bean**:
   - Kết nối UserDetailsService với PasswordEncoder
   - Xác thực user khi đăng nhập

3. **SecurityFilterChain Bean**:
   - Cấu hình URL patterns và quyền truy cập
   - Public URLs: `/`, `/home`, `/css/**`, `/tours`, `/register`, `/login`
   - Admin-only URLs: `/tours/admin/**`, `/bookings/admin/**`
   - Authenticated URLs: `/bookings/my-bookings`
   - Form login: `/login` → `/login-process` → `/home`
   - Logout: `/logout` → xóa session và cookie → `/login?logout=true`

#### CustomUserDetails.java
**Vai trò**: Implement interface `UserDetails` để Spring Security hiểu cách lấy thông tin user.

**Methods**:
- `getAuthorities()`: Trả về danh sách roles của user
- `getPassword()`: Trả về mật khẩu đã mã hóa
- `getUsername()`: Trả về username
- `isAccountNonExpired()`, `isAccountNonLocked()`, etc.: Các method kiểm tra trạng thái tài khoản

#### CustomUserDetailsService.java
**Vai trò**: Implement interface `UserDetailsService` để Spring Security biết cách load user từ database.

**Method**:
- `loadUserByUsername(String username)`: Tìm user trong database và trả về CustomUserDetails

### 6. Configuration Layer (Tầng Cấu Hình)

#### DataInitializer.java
**Vai trò**: Tự động khởi tạo dữ liệu cơ bản khi ứng dụng khởi động.

**Chức năng**:
- Implement `CommandLineRunner`: Chạy sau khi Spring Boot khởi động xong
- Tạo ROLE_ADMIN và ROLE_USER nếu chưa có
- Tạo tài khoản admin mặc định (username: admin, password: admin123)

**Lợi ích**: Đảm bảo hệ thống luôn có dữ liệu cơ bản để hoạt động

#### application.properties
**Vai trò**: File cấu hình toàn bộ ứng dụng.

**Các cấu hình chính**:
- **Server**: Port 8080
- **Database**: MySQL connection string, username, password
- **JPA**: Hibernate ddl-auto=update (tự động tạo/update schema)
- **Email**: Gmail SMTP configuration
- **File Upload**: Giới hạn kích thước file upload

### 7. DTO Layer (Data Transfer Objects)

#### RegisterRequest.java
**Vai trò**: Object để nhận dữ liệu từ form đăng ký.

**Lý do sử dụng DTO**: 
- Tách biệt Entity với form data
- Có thể validate riêng mà không ảnh hưởng đến Entity
- Bảo mật tốt hơn (không expose toàn bộ Entity)

### 8. View Layer (Tầng Giao Diện)

Templates sử dụng Thymeleaf để render HTML động.

#### Cấu trúc Templates:
- `home.html`: Trang chủ
- `login.html`: Form đăng nhập
- `register.html`: Form đăng ký
- `tours/list.html`: Danh sách tours với filter
- `tours/detail.html`: Chi tiết tour
- `tours/admin.html`: Quản lý tours (admin)
- `tours/form.html`: Form thêm/sửa tour
- `bookings/form.html`: Form đặt tour
- `bookings/detail.html`: Chi tiết booking
- `bookings/my-bookings.html`: Danh sách booking của user
- `bookings/admin.html`: Quản lý bookings (admin)
- `bookings/by-tour.html`: Danh sách booking theo tour

**Thymeleaf Features**:
- `th:text`: Hiển thị text động
- `th:each`: Vòng lặp
- `th:if`, `th:unless`: Điều kiện
- `sec:authorize`: Kiểm tra quyền (Spring Security integration)
- `th:href`: Link động

## Luồng Hoạt Động Của Hệ Thống

### 1. Luồng Đăng Ký User

```
Client → GET /register → AuthController.registerPage()
       → Hiển thị form đăng ký
       
Client → POST /register-process → AuthController.registerProcess()
       → Validate dữ liệu (@Valid)
       → UserService.registerUser()
         → Kiểm tra username tồn tại
         → Mã hóa password (BCrypt)
         → Gán ROLE_USER
         → Lưu vào database
       → Redirect về /login
```

### 2. Luồng Đăng Nhập

```
Client → GET /login → AuthController.loginPage()
       → Hiển thị form đăng nhập
       
Client → POST /login-process → Spring Security xử lý
       → CustomUserDetailsService.loadUserByUsername()
         → Tìm user trong database
         → Trả về CustomUserDetails
       → DaoAuthenticationProvider xác thực
         → So sánh password (BCrypt)
       → Tạo session và cookie JSESSIONID
       → Redirect về /home
```

### 3. Luồng Đặt Tour

```
Client → GET /tours → TourController.listTours()
       → TourService.getUpcomingTours()
       → Hiển thị danh sách tours
       
Client → Click "Đặt tour" → GET /bookings/new/{tourId}
       → BookingController.bookingForm()
       → Hiển thị form đặt tour
       
Client → POST /bookings/create → BookingController.createBooking()
       → Validate dữ liệu
       → BookingService.createBooking()
         → Kiểm tra số chỗ còn lại
         → Tính tổng giá
         → Lưu booking
         → Gửi email xác nhận
       → Redirect về /bookings/{id}
```

### 4. Luồng Quản Lý Tour (Admin)

```
Admin → GET /tours/admin → TourController.adminTours()
      → TourService.getAllTours()
      → Hiển thị danh sách tours
      
Admin → GET /tours/admin/new → TourController.newTourForm()
      → Hiển thị form thêm tour
      
Admin → POST /tours/admin/save → TourController.saveTour()
      → Validate dữ liệu
      → Upload ảnh (nếu có)
      → TourService.saveTour()
      → Redirect về /tours/admin
```

## Bảo Mật

### 1. Password Encryption

- Sử dụng BCrypt để mã hóa mật khẩu
- BCrypt là one-way hashing, không thể giải mã ngược
- Mỗi lần hash cùng một password sẽ cho kết quả khác nhau (salt tự động)
- Khi đăng nhập, Spring Security so sánh password input với hash trong database

### 2. Role-Based Access Control (RBAC)

- ROLE_ADMIN: Quyền quản trị, có thể truy cập `/tours/admin/**` và `/bookings/admin/**`
- ROLE_USER: Quyền người dùng thông thường, có thể đặt tour và xem booking của mình
- Anonymous: Chỉ có thể xem tours và đặt tour (không cần đăng nhập)

### 3. Session Management

- Spring Security tự động tạo session khi user đăng nhập
- Session ID được lưu trong cookie JSESSIONID
- Khi logout, session và cookie được xóa

### 4. CSRF Protection

- Spring Security mặc định bật CSRF protection
- Logout được cấu hình để chấp nhận GET request (cho đơn giản, production nên dùng POST)

## Cài Đặt và Chạy Ứng Dụng

### Yêu Cầu Hệ Thống

- Java 17 hoặc cao hơn
- MySQL 8.0 hoặc cao hơn
- Gradle 7.0+ (hoặc sử dụng Gradle Wrapper)
- IDE: IntelliJ IDEA, Eclipse, hoặc VS Code

### Bước 1: Clone Repository

```bash
git clone https://github.com/everlong123/LTW.git
cd LTW/demo
```

### Bước 2: Cấu Hình Database

1. Tạo database MySQL:
```sql
CREATE DATABASE tour_db;
```

2. Cập nhật `application.properties`:
   ```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tour_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

### Bước 3: Cấu Hình Email (Tùy Chọn)

Nếu muốn sử dụng tính năng gửi email xác nhận:

1. Tạo App Password từ Google Account
2. Cập nhật `application.properties`:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Lưu ý**: Nếu không cấu hình email, hệ thống vẫn hoạt động bình thường nhưng sẽ không gửi được email xác nhận.

### Bước 4: Chạy Ứng Dụng

**Windows**:
   ```bash
   .\gradlew.bat bootRun
   ```

**Linux/Mac**:
```bash
./gradlew bootRun
```

Hoặc chạy từ IDE:
- Mở project trong IDE
- Tìm class `DemoApplication.java`
- Run as Spring Boot Application

### Bước 5: Truy Cập Ứng Dụng

- Trang chủ: http://localhost:8080
- Danh sách tours: http://localhost:8080/tours
- Đăng ký: http://localhost:8080/register
- Đăng nhập: http://localhost:8080/login

### Tài Khoản Mặc Định

Sau khi chạy ứng dụng lần đầu, hệ thống tự động tạo:
- **Username**: admin
- **Password**: admin123
- **Role**: ROLE_ADMIN

## Các Tính Năng Chính

### Dành Cho Khách Hàng

1. **Xem Danh Mục Tour**
   - Xem tất cả tours sắp khởi hành
   - Xem chi tiết từng tour (ảnh, giá, địa điểm, số ngày, mô tả)

2. **Lọc Tour**
   - Lọc theo địa điểm
   - Lọc theo khoảng giá (min - max)
   - Lọc theo số ngày
   - Có thể kết hợp nhiều điều kiện

3. **Đặt Tour Online**
   - Chọn tour muốn đặt
   - Nhập thông tin cá nhân (họ tên, email, SĐT, địa chỉ)
   - Chọn số lượng người đi
   - Xem tổng giá tự động tính
   - Xác nhận đặt tour
   - Nhận email xác nhận tự động

4. **Xem Lịch Sử Đặt Tour**
   - Xem tất cả tours đã đặt (yêu cầu đăng nhập)
   - Xem chi tiết từng booking
   - Xem trạng thái thanh toán

### Dành Cho Admin

1. **Quản Lý Tour**
   - Thêm tour mới (upload ảnh, nhập đầy đủ thông tin)
   - Chỉnh sửa tour
   - Xóa tour
   - Xem danh sách tất cả tours

2. **Quản Lý Đặt Tour**
   - Xem tất cả bookings
   - Xem bookings theo từng tour
   - Xem thông tin khách hàng (họ tên, email, SĐT, địa chỉ)
   - Xem số người đặt, tổng giá
   - Cập nhật trạng thái thanh toán (đã thanh toán / chưa thanh toán)

3. **Kiểm Soát Số Lượng Khách**
   - Hệ thống tự động tính số chỗ còn lại
   - Không cho phép đặt vượt quá số chỗ còn lại
   - Hiển thị số chỗ còn lại trên giao diện

## Dependency Injection và Spring IoC Container

Spring Framework sử dụng Inversion of Control (IoC) Container để quản lý các objects (beans) trong ứng dụng.

### Các Annotation Quan Trọng

- `@Component`: Đánh dấu class là Spring bean (generic)
- `@Service`: Đánh dấu class là service bean (business logic)
- `@Repository`: Đánh dấu class là repository bean (data access)
- `@Controller`: Đánh dấu class là controller bean (web layer)
- `@Autowired`: Tự động inject dependency

### Ví Dụ Dependency Injection

```java
@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;  // Spring tự động inject
}
```

Khi Spring khởi động:
1. Tạo instance của `TourRepository`
2. Tạo instance của `TourService`
3. Inject `TourRepository` vào `TourService`

## Transaction Management

Hệ thống sử dụng `@Transactional` để quản lý transactions:

- `@Transactional` trên method: Method sẽ chạy trong một transaction
- Nếu có lỗi, tất cả thay đổi sẽ được rollback
- Đảm bảo tính nhất quán dữ liệu (ACID)

**Ví dụ**:
```java
@Transactional
public Booking createBooking(...) {
    // Tất cả các thao tác database trong method này
    // sẽ chạy trong một transaction
    // Nếu có lỗi, tất cả sẽ rollback
}
```

## Validation

Hệ thống sử dụng Jakarta Bean Validation để validate dữ liệu:

- **Entity Level**: Validation annotations trên Entity fields (`@NotBlank`, `@Size`, `@NotNull`)
- **Controller Level**: Sử dụng `@Valid` để trigger validation
- **HTML Level**: HTML5 validation (minlength, maxlength, required)

**Luồng Validation**:
1. Client submit form
2. Controller nhận request với `@Valid`
3. Spring validate dữ liệu
4. Nếu có lỗi, trả về form với error messages
5. Nếu hợp lệ, tiếp tục xử lý

## Email Service

Hệ thống tích hợp Spring Mail để gửi email xác nhận đặt tour:

- Sử dụng Gmail SMTP
- Gửi email tự động sau khi đặt tour thành công
- Nội dung email bao gồm: mã booking, thông tin tour, thông tin khách, tổng giá, trạng thái thanh toán

**Lưu ý**: Cần cấu hình App Password từ Google Account để sử dụng Gmail SMTP.

## File Upload

Hệ thống hỗ trợ upload ảnh tour:

- Lưu ảnh vào `static/images/tours/`
- Tên file được đổi thành timestamp để tránh trùng lặp
- Giới hạn kích thước: 10MB (cấu hình trong `application.properties`)

## Best Practices Được Áp Dụng

1. **Separation of Concerns**: Tách biệt rõ ràng Controller, Service, Repository
2. **Dependency Injection**: Sử dụng Spring IoC để quản lý dependencies
3. **Transaction Management**: Sử dụng `@Transactional` cho các thao tác database
4. **Validation**: Validate ở nhiều tầng (Entity, Controller, HTML)
5. **Security**: Sử dụng Spring Security với role-based access control
6. **Error Handling**: Try-catch và error messages rõ ràng
7. **Code Organization**: Package structure rõ ràng, dễ maintain

## Troubleshooting

### Lỗi Kết Nối Database

**Triệu chứng**: Ứng dụng không khởi động, lỗi "Cannot connect to database"

**Giải pháp**:
1. Kiểm tra MySQL đã chạy chưa
2. Kiểm tra username/password trong `application.properties`
3. Kiểm tra database `tour_db` đã tồn tại chưa

### Lỗi Port Đã Được Sử Dụng

**Triệu chứng**: "Port 8080 is already in use"

**Giải pháp**:
1. Đổi port trong `application.properties`: `server.port=8081`
2. Hoặc tắt ứng dụng đang chạy trên port 8080

### Lỗi Email Không Gửi Được

**Triệu chứng**: Booking thành công nhưng không nhận được email

**Giải pháp**:
1. Kiểm tra cấu hình email trong `application.properties`
2. Kiểm tra App Password từ Google Account
3. Xem log console để biết lỗi cụ thể

### Lỗi Upload Ảnh

**Triệu chứng**: Không upload được ảnh tour

**Giải pháp**:
1. Kiểm tra thư mục `static/images/tours/` đã tồn tại chưa
2. Kiểm tra quyền ghi file
3. Kiểm tra kích thước file không vượt quá 10MB

## Kết Luận

Đây là một hệ thống quản lý tour du lịch hoàn chỉnh, được xây dựng theo các best practices của Spring Boot. Hệ thống áp dụng kiến trúc MVC rõ ràng, bảo mật với Spring Security, và sử dụng JPA để quản lý database một cách hiệu quả.

Dự án này phù hợp để học tập và hiểu rõ cách xây dựng một ứng dụng web enterprise với Spring Boot, từ cơ bản đến nâng cao.



