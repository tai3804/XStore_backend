package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.LoginRequest;
import iuh.fit.xstore.dto.request.RegisterRequest;
import iuh.fit.xstore.dto.request.ResetPasswordRequest;
import iuh.fit.xstore.dto.request.SendOtpRequest;
import iuh.fit.xstore.dto.request.VerifyOtpRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Account;
import iuh.fit.xstore.model.Cart;
import iuh.fit.xstore.model.Role;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.AccountRepository;
import iuh.fit.xstore.repository.CartRepository;
import iuh.fit.xstore.repository.UserRepository;
import iuh.fit.xstore.security.UserDetail;
import iuh.fit.xstore.service.JwtService;
import iuh.fit.xstore.service.OtpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AccountRepository accountRepository;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtUtil;
    private OtpService otpService;
    private final CartRepository cartRepository;

    @Value("${google.client.id}")
    private String googleClientId;

    @Autowired
    public AuthController(AccountRepository accountRepository, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtUtil, OtpService otpService, CartRepository cartRepository) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.otpService = otpService;
        this.cartRepository = cartRepository;
    }

    // (Giu nguyen /login)
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        try {
            log.info("Dang nhap voi username: {}", request.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetail);
            User user = userRepository.getByAccountUsername(request.getUsername());
            log.info("Dang nhap thanh cong, user: {}", request.getUsername());
            var username = SecurityContextHolder.getContext().getAuthentication();
            log.warn("return: ", username);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            return new ApiResponse<>(SuccessCode.LOGIN_SUCCESSFULLY, data);
        } catch (UsernameNotFoundException e) {
            log.error("User khong tim thay: {}", e.getMessage());
            return new ApiResponse<>(ErrorCode.USER_NOT_FOUND);
        } catch (BadCredentialsException e) {
            log.error("Ten dang nhap hoac mat khau khong chinh xac");
            return new ApiResponse<>(ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (Exception e) {
            log.error("Loi dang nhap: {}", e.getMessage());
            e.printStackTrace();
            return new ApiResponse<>(ErrorCode.UNKNOWN_ERROR);
        }
    }

    // (Giu nguyen /register)
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest request) {
        log.info("Dang ky user moi: {}", request.getUsername());
        if (userRepository.existsByAccountUsername(request.getUsername())) {
            log.warn("User {} da ton tai", request.getUsername());
            return new ApiResponse<>(ErrorCode.USER_EXISTED);
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .email(request.getEmail())
                .phone(request.getPhone())
                .account(Account.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.CUSTOMER)
                        .build()
                )
                .cart(Cart.builder()
                        .total(0)
                        .build()
                )
                .build();
        userRepository.save(user);
        return new ApiResponse<>(SuccessCode.REGISTER_SUCCESSFULLY, user);
    }

    // ================= RESET PASSWORD (DA SUA THONG MINH HON) =================
    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        log.info("Dat lai mat khau cho: {}", request.getUsername());
        // DTO request.getUsername() bay gio co the la EMAIL hoac SDT
        String contact = request.getUsername();

        User user = userRepository.findByEmail(contact)
                .or(() -> userRepository.findByPhone(contact)) // Thu tim SDT
                .orElseThrow(() -> {
                    log.error("Khong tim thay user voi contact: {}", contact);
                    return new AppException(ErrorCode.USER_NOT_FOUND);
                });

        Account account = user.getAccount();
        if (account == null) {
            log.error("Khong tim thay account cho user: {}", contact);
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
        log.info("Dat lai mat khau thanh cong cho: {}", contact);

        return new ApiResponse<>(SuccessCode.RESET_PASSWORD_SUCCESSFULLY, user);
    }

    // ================= PHONE OTP VERIFICATION =================
    @PostMapping("/send-otp")
    public ApiResponse<?> sendOtp(@RequestBody SendOtpRequest request) {
        try {
            log.info("Gui OTP den: {}", request.getPhoneNumber());
            if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
                log.warn("So dien thoai khong hop le");
                return new ApiResponse<>(ErrorCode.INVALID_INPUT);
            }

            String otp = otpService.generateOtp(request.getPhoneNumber());
            
            Map<String, Object> data = new HashMap<>();
            data.put("message", "OTP sent successfully to " + request.getPhoneNumber());
            data.put("expiryMinutes", 5);

            log.info("Gui OTP thanh cong den: {}", request.getPhoneNumber());
            return new ApiResponse<>(SuccessCode.OTP_SEND_SUCCESSFULLY, data);
        } catch (IllegalArgumentException e) {
            log.error("Dinh dang so dien thoai khong hop le: {}", e.getMessage());
            return new ApiResponse<>(ErrorCode.INVALID_INPUT);
        } catch (Exception e) {
            log.error("Loi khi gui OTP: {}", e.getMessage());
            return new ApiResponse<>(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @PostMapping("/verify-otp")
    public ApiResponse<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            log.info("Kiem tra OTP cho: {}", request.getPhoneNumber());
            if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
                log.warn("So dien thoai khong hop le");
                return new ApiResponse<>(ErrorCode.INVALID_INPUT);
            }
            if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
                log.warn("OTP khong hop le");
                return new ApiResponse<>(ErrorCode.INVALID_INPUT);
            }

            // Verify OTP
            boolean isValid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp().trim());
            
            if (!isValid) {
                log.warn("OTP khong hop le hoac het han cho: {}", request.getPhoneNumber());
                return new ApiResponse<>(ErrorCode.OTP_INVALID_OR_EXPIRATION);
            }

            // OTP verified - update user's phone and verification status
            // This is done when user is logged in and updates their account
            log.info("Kiem tra OTP thanh cong cho: {}", request.getPhoneNumber());
            
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Phone verified successfully");
            data.put("phoneNumber", request.getPhoneNumber());

            return new ApiResponse<>(SuccessCode.OTP_VALID, data);
        } catch (Exception e) {
            log.error("Error verifying OTP: {}", e.getMessage());
            return new ApiResponse<>(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @PostMapping("/google-login")
    public ApiResponse<?> googleLogin(@RequestBody Map<String, String> request) {
        try {
            log.info("Dang nhap Google");
            String token = request.get("token");
            if (token == null || token.trim().isEmpty()) {
                log.warn("Token Google khong hop le");
                return new ApiResponse<>(ErrorCode.INVALID_INPUT);
            }

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                log.info("Dang nhap Google thanh cong, email: {}", email);

                // Split name into first and last
                String[] names = name.split(" ", 2);
                String firstName = names[0];
                String lastName = names.length > 1 ? names[1] : "";

                // Check if user exists
                Optional<User> existingUser = userRepository.findByEmail(email);
                User user;
                if (existingUser.isPresent()) {
                    user = existingUser.get();
                } else {
                    // Create new user
                    Account account = Account.builder()
                            .username(email)
                            .password(passwordEncoder.encode("defaultpassword")) // default password for OAuth users
                            .role(Role.CUSTOMER)
                            .build();
                    account = accountRepository.save(account);

                    Cart cart = Cart.builder()
                            .total(0)
                            .build();
                    cart = cartRepository.save(cart);

                    user = User.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .account(account)
                            .cart(cart)
                            .build();
                    user = userRepository.save(user);
                }

                // Create JWT
                UserDetail userDetail = new UserDetail(user.getAccount());
                String jwt = jwtUtil.generateToken(userDetail);

                Map<String, Object> data = new HashMap<>();
                data.put("token", jwt);
                data.put("user", user);
                return new ApiResponse<>(SuccessCode.LOGIN_SUCCESSFULLY, data);
            } else {
                log.error("Token Google khong hop le hoac het han");
                return new ApiResponse<>(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            log.error("Loi dang nhap Google: {}", e.getMessage());
            return new ApiResponse<>(ErrorCode.UNKNOWN_ERROR);
        }
    }
}
