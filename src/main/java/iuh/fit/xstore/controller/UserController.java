package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.ChangePasswordRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    ApiResponse<List<User>> getUsers () {
        log.info("Lay danh sach tat ca user");
        List<User> users = userService.findAll();
        log.info("Lay danh sach user thanh cong, so luong: {}", users.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, users);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUser(@PathVariable("id") int id) {
        log.info("Lay user theo ID: {}", id);
        User user = userService.findById(id);
        log.info("Lay user theo ID thanh cong");
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, user);
    }

    // GET USER BY USERNAME
    @GetMapping("/username/{username}")
    public ApiResponse<User> getUserByUsername(@PathVariable("username") String username) {
        log.info("Lay user theo username: {}", username);
        User user = userService.findByUsername(username);
        log.info("Lay user theo username thanh cong");
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, user);
    }

    // SEARCH USERS
    @GetMapping("/search/query")
    public ApiResponse<List<User>> searchUsers(@RequestParam("query") String query) {
        log.info("Tim kiem user voi tu khoa: {}", query);
        List<User> users = userService.searchUsers(query);
        log.info("Tim kiem user thanh cong, so luong: {}", users.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, users);
    }

    @PostMapping()
    ApiResponse<User> createUser(@RequestBody User user) {
        log.info("Tao user moi: {}", user.getFirstName());
        User createdUser = userService.createUser(user);
        log.info("Tao user thanh cong, ID: {}", createdUser.getId());
        return new ApiResponse<>(SuccessCode.USER_CREATED, createdUser);
    }

    @PutMapping("/{id}")
    ApiResponse<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
        log.info("Cap nhat user ID: {}", id);
        user.setId(id);
        User updatedUser = userService.updateUser(id, user);
        log.info("Cap nhat user thanh cong");
        return new ApiResponse<>(SuccessCode.USER_UPDATED, updatedUser);
    }


    @DeleteMapping("/{id}")
    ApiResponse<Integer> deleteUser(@PathVariable("id") int id) {
        log.info("Xoa user ID: {}", id);
        userService.deleteUser(id);
        log.info("Xoa user thanh cong");
        return new ApiResponse<>(SuccessCode.USER_DELETED, id);
    }
    // === ENDPOINT MOI DE DOI MAT KHAU ===
    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody ChangePasswordRequest request) {
        log.info("Thay doi mat khau");
        userService.changePassword(request);
        log.info("Thay doi mat khau thanh cong");
        // Dung lai SuccessCode RESET_PASSWORD_SUCCESSFULLY
        return new ApiResponse<>(SuccessCode.RESET_PASSWORD_SUCCESSFULLY, "Doi mat khau thanh cong");
    }

    // === ENDPOINT THONG KE KHACH HANG MOI THEO KHOANG THOI GIAN ===
    @GetMapping("/statistics/new-customers")
    public ApiResponse<Long> getNewCustomersCount(
            @RequestParam(value = "period", required = false, defaultValue = "month") String period) {
        log.info("Lay thong ke khach hang moi, period: {}", period);
        long count = userService.getNewCustomersCount(period);
        log.info("Lay thong ke khach hang moi thanh cong, count: {}", count);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, count);
    }
}
