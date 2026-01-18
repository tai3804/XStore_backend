package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.request.ChangePasswordRequest;
import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.*;
import iuh.fit.xstore.repository.AccountRepository;
import iuh.fit.xstore.repository.CartRepository;
import iuh.fit.xstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final CartRepository cartRepo;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepo.findAll();
    }


    public User findById(int id) {
        return userRepo.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public User findByUsername(String username) {
        return userRepo.findByAccountUsername((username))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public List<User> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }
        return userRepo.searchUsers(query.toLowerCase());
    }

    //tao user

    public User createUser(User user) {
        log.info("Creating user with email: {}, firstName: {}", user.getEmail(), user.getFirstName());
        try {
            // --- Kiểm tra tài khoản đã tồn tại ---
            if (userRepo.existsByEmail(user.getEmail())){
                log.error("User with email {} already exists", user.getEmail());
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }

            // --- Kiểm tra password ---
            if (user.getAccount() == null || user.getAccount().getPassword() == null || user.getAccount().getPassword().trim().isEmpty()) {
                log.error("Password is empty for user {}", user.getEmail());
                throw new AppException(ErrorCode.PASSWORD_EMPTY);
            }

            // --- Tạo mới Account ---
            Account account = Account.builder()
                    .username(user.getAccount().getUsername() != null ? user.getAccount().getUsername() : user.getEmail())
                    .password(passwordEncoder.encode(user.getAccount().getPassword()))
                    .role(user.getAccount().getRole() != null ? user.getAccount().getRole() : Role.CUSTOMER)
                    .build();
            account = accountRepo.save(account);
            log.info("Account created with username: {}", account.getUsername());

            // --- Tạo giỏ hàng mặc định ---
            Cart cart = Cart.builder()
                    .total(0)
                    .build();
            cart = cartRepo.save(cart); // Save cart first
            log.info("Cart created with id: {}", cart.getId());

            // --- Tạo mới User ---
            User newUser = User.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .dob(user.getDob())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .userType(user.getUserType() != null ? user.getUserType() : UserType.COPPER)
                    .point(0)
                    .account(account)
                    .cart(cart)
                    .build();

            User savedUser = userRepo.save(newUser);
            log.info("User created successfully with id: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            log.error("Error creating user: ", e);
            throw e;
        }
    }



    // Cập nhật user
    public User updateUser(int id, User user) {
        User existedUser = userRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Update profile fields only if provided (not null)
        if (user.getFirstName() != null) {
            existedUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existedUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            existedUser.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            existedUser.setPhone(user.getPhone());
        }
        if (user.getAvatar() != null) {
            existedUser.setAvatar(user.getAvatar());
        }

        // Update user type and point if provided
        if (user.getUserType() != null) {
            existedUser.setUserType(user.getUserType());
        }
        if (user.getPoint() != 0 || (user.getPoint() == 0 && existedUser.getPoint() != 0)) {
            existedUser.setPoint(user.getPoint());
        }

        // Update account role if provided
        if (user.getRole() != null && existedUser.getAccount() != null) {
            existedUser.getAccount().setRole(user.getRole());
        }

        return userRepo.save(existedUser);
    }

    // Xoá user
    public int deleteUser(int id) {
        findById(id);
        userRepo.deleteById(id);
        return id;
    }
    // === PHƯƠNG THỨC MỚI ĐỂ ĐỔI MẬT KHẨU ===
    public void changePassword(ChangePasswordRequest request) {
        // 1. Lấy username của người dùng đang đăng nhập
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        // 2. Tìm tài khoản
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 3. Xác thực mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        // 4. Kiểm tra mật khẩu mới
        if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            throw new AppException(ErrorCode.PASSWORD_EMPTY);
        }

        // 5. Cập nhật mật khẩu mới (đã mã hóa)
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 6. Lưu lại vào database
        accountRepo.save(account);
    }

    /**
     * Cập nhật điểm thành viên sau khi mua hàng
     * Quy tắc: Mỗi 1000₫ = 1 điểm
     * Xếp hạng:
     * - COPPER (Đồng): 0 - 99 điểm
     * - SILVER (Bạc): 100 - 199 điểm
     * - GOLD (Vàng): 200 - 499 điểm
     * - PLATINUM (Bạch kim): >= 500 điểm (không tích điểm nữa)
     */
    public void updatePointsAndRank(int userId, double orderTotal) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Nếu đã là PLATINUM thì không tích điểm nữa
        if (user.getUserType() == UserType.PLATINUM) {
            log.info("User {} is already PLATINUM, no points added", userId);
            return;
        }

        // Tính điểm mới: Mỗi 1000₫ = 1 điểm
        int pointsToAdd = (int) (orderTotal / 1000);
        int currentPoints = user.getPoint();
        int newPoints = currentPoints + pointsToAdd;

        // Xác định hạng mới
        UserType newRank = determineUserRank(newPoints);
        UserType oldRank = user.getUserType();

        // Cập nhật điểm và hạng
        user.setPoint(newPoints);
        user.setUserType(newRank);
        userRepo.save(user);

        log.info("User {} earned {} points (Total: {} points) | Rank: {} -> {}", 
                userId, pointsToAdd, newPoints, oldRank, newRank);
    }

    /**
     * Xác định hạng thành viên dựa trên tổng điểm
     */
    private UserType determineUserRank(int points) {
        if (points >= 500) {
            return UserType.PLATINUM; // Bạch kim: >= 500 điểm
        } else if (points >= 200) {
            return UserType.GOLD;     // Vàng: 200-499 điểm
        } else if (points >= 100) {
            return UserType.SILVER;   // Bạc: 100-199 điểm
        } else {
            return UserType.COPPER;   // Đồng: 0-99 điểm
        }
    }

    /**
     * Đếm số khách hàng mới trong khoảng thời gian
     * @param period "day", "month", hoặc "year"
     * @return số lượng khách hàng mới
     */
    public long getNewCustomersCount(String period) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime startDate;
        java.time.LocalDateTime endDate = now;

        switch (period.toLowerCase()) {
            case "day":
                startDate = now.toLocalDate().atStartOfDay();
                break;
            case "year":
                startDate = java.time.LocalDateTime.of(now.getYear(), 1, 1, 0, 0);
                break;
            case "month":
            default:
                startDate = java.time.LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
                break;
        }

        return userRepo.countNewCustomersBetween(startDate, endDate);
    }
}
