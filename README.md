# XStore Backend - Hệ thống quản lý cửa hàng thời trang

Backend REST API cho ứng dụng XStore - Hệ thống thương mại điện tử bán quần áo trực tuyến.

## Mục lục

- [Giới thiệu](#giới-thiệu)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt và chạy](#cài-đặt-và-chạy)
- [Cấu hình](#cấu-hình)
- [API Endpoints](#api-endpoints)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Tính năng chính](#tính-năng-chính)
- [Database](#database)
- [Testing](#testing)
- [Deployment](#deployment)

## Giới thiệu

XStore Backend là REST API server được xây dựng với Spring Boot, cung cấp các dịch vụ backend cho ứng dụng bán quần áo trực tuyến. Hệ thống hỗ trợ:

- Quản lý sản phẩm với biến thể (màu sắc, kích thước)
- Quản lý giỏ hàng và đơn hàng
- Xác thực và phân quyền người dùng
- Tích hợp thanh toán VNPay
- Quản lý kho hàng và tồn kho
- Hệ thống giảm giá và khuyến mãi
- Export PDF hóa đơn

## Công nghệ sử dụng

### Core Framework
- **Spring Boot 3.3.4** - Framework chính
- **Java 21** - Ngôn ngữ lập trình
- **Maven** - Quản lý dependencies

### Spring Modules
- **Spring Data JPA** - ORM và database access
- **Spring Security** - Authentication & Authorization
- **Spring Web** - REST API
- **Spring OAuth2 Resource Server** - JWT & OAuth2

### Database
- **MariaDB** - Database chính (local)
- **MySQL** - Database production (railway)

### Libraries
- **Lombok** - Giảm boilerplate code
- **Google API Client** - Google OAuth integration
- **OpenPDF** - PDF generation
- **JavaMail** - Email service
- **JWT** - JWT token processing

### DevOps
- **Docker** - Containerization
- **Maven Wrapper** - Build tool

## Yêu cầu hệ thống

- Java JDK 21 hoặc cao hơn
- Maven 3.8+ (hoặc dùng Maven Wrapper có sẵn)
- MariaDB 10.6+ hoặc MySQL 8.0+
- RAM tối thiểu 2GB
- Disk space: 500MB

## Cài đặt và chạy

### 1. Clone repository

```bash
git clone https://github.com/tai3804/XStore_backend
cd XStore_backend
```

### 2. Cấu hình môi trường

Tạo file `.env` trong thư mục root:

```properties
#Spring
APP_PORT=8080
#Database
DB_URL=jdbc:mariadb://localhost:3306/xstoredb?createDatabaseIfNotExist=true
DB_PASSWORD=YOUR_PASSWORD

#Mail
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=tranthanhtai1928@gmail.com
MAIL_PASSWORD=djngyzqgyzygwwkd

#JWT
JWT_KEY=982759fe9032c9794bacef867d65dc1084b29c6685176f77ee9d7765d0a1ad3d

#Google
GOOGLE_CLIENT_ID=20028934029-urn06qotve6ot72vc537v1voujlm2h9g.apps.googleusercontent.com

#Gemini
SECRET_KEY=982759fe9032c9794bacef867d65dc1084b29c6685176f77ee9d7765d0a1ad3d
GEMINI_API_KEY=AIzaSyAeujUb1bJLH_j1RQV_xDPUjUJIpUtHyOI

#vn pay
VNP_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
VNP_RETURN_URL=http://localhost:5173/payment-return
VNP_IPN_URL=https://uninsidious-toothlike-karl.ngrok-free.dev/api/payment/ipn
VNP_TMN_CODE=DJ4JK2A0
VNP_HASH_SECRET=D6JWBE6AWGZZPJZ1UUD4WKV9N0V65KZ8
VNP_VERSION=2.1.0
VNP_COMMAND=pay
VNP_ORDER_TYPE=orther
```

### 3. Build và chạy

**Sử dụng Maven Wrapper (khuyến nghị):**

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**Hoặc build thành JAR:**

```bash
# Build
.\mvnw.cmd clean package -DskipTests

# Run
java -jar target/X-Store-0.0.1-SNAPSHOT.war
```

### 4. Kiểm tra server

Server chạy tại: `http://localhost:8080`

Test endpoint: `http://localhost:8080/api/products/test`

## API Endpoints

### Authentication & Users

```
POST   /api/auth/register          - Đăng ký tài khoản mới
POST   /api/auth/login             - Đăng nhập
POST   /api/auth/google            - Đăng nhập Google OAuth
POST   /api/auth/send-otp          - Gửi mã OTP
POST   /api/auth/verify-otp        - Xác thực OTP
POST   /api/auth/reset-password    - Đặt lại mật khẩu

GET    /api/users                  - Lấy danh sách users
GET    /api/users/{id}             - Lấy user theo ID
PUT    /api/users/{id}             - Cập nhật user
DELETE /api/users/{id}             - Xóa user
```

### Products

```
GET    /api/products               - Lấy tất cả sản phẩm
GET    /api/products/{id}          - Lấy sản phẩm theo ID
GET    /api/products/type/{typeId} - Lấy sản phẩm theo loại
POST   /api/products               - Tạo sản phẩm mới
POST   /api/products/{id}/upload   - Tạo sản phẩm với ảnh
PUT    /api/products/{id}          - Cập nhật sản phẩm
PUT    /api/products/{id}/upload   - Cập nhật với ảnh
DELETE /api/products/{id}          - Xóa sản phẩm
GET    /api/products/search?q=     - Tìm kiếm sản phẩm
POST   /api/products/filter        - Lọc sản phẩm nâng cao
```

### Product Info (Biến thể)

```
GET    /api/products/{productId}/info           - Lấy biến thể của sản phẩm
GET    /api/products/{productId}/colors         - Lấy màu sắc
GET    /api/products/{productId}/sizes          - Lấy kích thước
POST   /api/products/{productId}/info/upload    - Tạo biến thể với ảnh
PUT    /api/products/info/{id}/upload           - Cập nhật biến thể
DELETE /api/products/info/{id}                  - Xóa biến thể
```

### Cart & Cart Items

```
GET    /api/carts                  - Lấy tất cả giỏ hàng
GET    /api/carts/{id}             - Lấy giỏ hàng theo ID
GET    /api/carts/user/{userId}    - Lấy giỏ hàng của user
POST   /api/carts                  - Tạo giỏ hàng mới
DELETE /api/carts/{id}             - Xóa giỏ hàng

GET    /api/cart-items             - Lấy tất cả cart items
GET    /api/cart-items/cart/{id}   - Lấy items của giỏ hàng
POST   /api/cart-items/add         - Thêm sản phẩm vào giỏ
PUT    /api/cart-items/{id}        - Cập nhật số lượng
DELETE /api/cart-items/{id}        - Xóa item khỏi giỏ
```

### Orders

```
GET    /api/orders                 - Lấy tất cả đơn hàng
GET    /api/orders/{id}            - Lấy đơn hàng theo ID
GET    /api/orders/user/{userId}   - Lấy đơn hàng của user
POST   /api/orders                 - Tạo đơn hàng mới
POST   /api/orders/checkout        - Thanh toán và tạo đơn
PUT    /api/orders/{id}/status     - Cập nhật trạng thái
DELETE /api/orders/{id}            - Xóa đơn hàng
POST   /api/orders/{id}/cancel     - Hủy đơn hàng
GET    /api/orders/{id}/pdf        - Tải PDF hóa đơn
```

### Stock Management

```
GET    /api/stocks                         - Lấy danh sách kho
GET    /api/stocks/{id}                    - Lấy kho theo ID
POST   /api/stocks                         - Tạo kho mới
PUT    /api/stocks/{id}                    - Cập nhật kho
DELETE /api/stocks/{id}                    - Xóa kho
GET    /api/stocks/{id}/items              - Lấy sản phẩm trong kho
POST   /api/stocks/{id}/items/set          - Đặt số lượng
POST   /api/stocks/{id}/items/increase     - Tăng số lượng
POST   /api/stocks/{id}/items/decrease     - Giảm số lượng
DELETE /api/stocks/{id}/items/{productId}  - Xóa sản phẩm khỏi kho
```

### Discounts

```
GET    /api/discounts              - Lấy tất cả giảm giá
GET    /api/discounts/{id}         - Lấy giảm giá theo ID
POST   /api/discounts              - Tạo giảm giá mới
PUT    /api/discounts/{id}         - Cập nhật giảm giá
DELETE /api/discounts/{id}         - Xóa giảm giá
```

### Product Types

```
GET    /api/product-types          - Lấy tất cả loại sản phẩm
GET    /api/product-types/{id}     - Lấy loại theo ID
POST   /api/product-types          - Tạo loại mới
PUT    /api/product-types/{id}     - Cập nhật loại
DELETE /api/product-types/{id}     - Xóa loại
```

### Response Format

Tất cả API responses đều có format:

```json
{
  "code": 200,
  "message": "Success message",
  "result": { ... }
}
```

Error response:

```json
{
  "code": 400,
  "message": "Error message",
  "result": null
}
```

## Cấu trúc dự án

```
XStore_backend/
├── src/
│   ├── main/
│   │   ├── java/iuh/fit/xstore/
│   │   │   ├── config/          # Cấu hình (Security, CORS, etc.)
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── request/     # Request DTOs
│   │   │   │   └── response/    # Response DTOs
│   │   │   ├── model/           # Entity models (JPA)
│   │   │   ├── repository/      # JPA Repositories
│   │   │   ├── security/        # Security (JWT, UserDetails)
│   │   │   ├── service/         # Business logic
│   │   │   └── XStoreApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application.yaml
│   │       ├── data.sql         # Initial data
│   │       └── fonts/           # Fonts for PDF
│   └── test/                    # Unit tests
├── uploads/                     # Uploaded images
│   └── products/
├── logs/                        # Application logs
├── .env                         # Environment variables
├── Dockerfile                   # Docker configuration
├── pom.xml                      # Maven dependencies
└── README.md
```

## Tính năng chính

### 1. Authentication & Authorization
- JWT-based authentication
- Google OAuth 2.0 integration
- Role-based access control (ADMIN, CUSTOMER)
- Password reset với OTP qua email

### 2. Product Management
- CRUD operations cho sản phẩm
- Upload và quản lý ảnh sản phẩm
- Biến thể sản phẩm (màu sắc, kích thước)
- Tìm kiếm và lọc sản phẩm nâng cao
- Quản lý loại sản phẩm

### 3. Shopping Cart
- Thêm/xóa/cập nhật sản phẩm trong giỏ
- Tự động tạo giỏ hàng cho user mới
- Quản lý số lượng và tính tổng tiền

### 4. Order Processing
- Checkout và tạo đơn hàng
- Theo dõi trạng thái đơn hàng
- Hủy đơn hàng
- Export PDF hóa đơn (với font tiếng Việt)
- Gửi email xác nhận đơn hàng

### 5. Payment Integration
- Tích hợp VNPay gateway
- Xử lý callback từ VNPay
- Cập nhật trạng thái thanh toán tự động

### 6. Stock Management
- Quản lý nhiều kho hàng
- Theo dõi tồn kho theo từng biến thể
- Cập nhật số lượng (tăng/giảm/đặt)
- Kiểm tra tồn kho trước khi bán

### 7. Discount System
- Tạo và quản lý mã giảm giá
- Áp dụng giảm giá theo phần trăm hoặc số tiền
- Hạn chế thời gian và số lượng sử dụng

### 8. Logging & Monitoring
- Comprehensive logging với SLF4J
- Log tất cả operations quan trọng
- Theo dõi lỗi và exceptions
- File logs tại `logs/logs.txt`

## Database

### Schema chính

**Tables:**
- `account` - Tài khoản đăng nhập
- `user` - Thông tin người dùng
- `product` - Sản phẩm
- `product_type` - Loại sản phẩm
- `product_info` - Biến thể (màu, size)
- `cart` - Giỏ hàng
- `cart_item` - Sản phẩm trong giỏ
- `order` - Đơn hàng
- `order_item` - Sản phẩm trong đơn
- `stock` - Kho hàng
- `stock_item` - Tồn kho
- `discount` - Giảm giá
- `address` - Địa chỉ giao hàng
- `otp` - Mã OTP

### Relationships
- User 1-1 Account (cascade)
- User 1-1 Cart (cascade)
- Cart 1-N CartItem
- User 1-N Order
- Order 1-N OrderItem
- Product 1-N ProductInfo
- Product N-1 ProductType
- Stock 1-N StockItem
- Stock 1-1 Address (cascade)

### Data Initialization
- `data.sql` tự động chạy khi database trống
- Tạo sẵn admin và customer mặc định
- Import dữ liệu sản phẩm mẫu

## Testing

```bash
# Run all tests
.\mvnw.cmd test

# Run specific test
.\mvnw.cmd test -Dtest=UserServiceTest

# Skip tests during build
.\mvnw.cmd package -DskipTests
```

## Deployment

### Docker

Build và run với Docker:

```bash
# Build image
docker build -t xstore-backend .

# Run container
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mariadb://host:3306/xstoredb \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=password \
  xstore-backend
```

### Railway/Cloud

1. Set environment variables
2. Configure database connection
3. Deploy từ Git repository
4. Cập nhật CORS origins trong `SecurityConfig.java`

### Production Checklist

- [ ] Đổi `spring.jpa.show-sql=false`
- [ ] Cập nhật JWT secret key (256-bit)
- [ ] Cấu hình HTTPS
- [ ] Enable rate limiting
- [ ] Setup monitoring và alerting
- [ ] Configure backup strategy
- [ ] Update CORS allowed origins
- [ ] Secure all sensitive endpoints

## Security

### Best Practices
- JWT tokens với expiration time
- Password hashing với BCrypt
- CORS configuration
- Input validation
- SQL injection prevention (JPA)
- XSS protection
- HTTPS for production

### Protected Endpoints
- Tất cả `/api/admin/**` yêu cầu ROLE_ADMIN
- `/api/users/**` yêu cầu authentication
- `/api/orders/{id}/pdf` chỉ owner hoặc admin

## Logging

Logs được ghi vào `logs/logs.txt` với format:

```
[timestamp] [level] [class] - message
```

**Levels:**
- `INFO` - Operations thành công
- `WARN` - Cảnh báo (không tìm thấy resource)
- `ERROR` - Lỗi nghiêm trọng

**Logged events:**
- Tất cả API requests
- Database operations
- Authentication events
- Payment processing
- File uploads
- Exceptions

## Troubleshooting

### Port đã được sử dụng

```bash
# Windows - Kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Database connection failed

- Kiểm tra MariaDB/MySQL đang chạy
- Verify username/password trong `.env`
- Kiểm tra database đã tạo chưa

### JWT token invalid

- Check JWT_SECRET trong `.env`
- Verify token chưa hết hạn
- Clear browser cookies/localStorage

## Support & Contact

- **Project**: XStore E-commerce Platform
- **Version**: 0.0.1-SNAPSHOT
- **Java Version**: 21
- **Spring Boot**: 3.3.4

## License

Copyright © 2026 XStore Team
