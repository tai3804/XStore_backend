package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.CreateCartRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.Cart;
import iuh.fit.xstore.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Lay tat ca gio hang
    @GetMapping
    public ResponseEntity<?> getAllCarts() {
        try {
            log.info("Lay danh sach tat ca gio hang");
            List<Cart> carts = cartService.getAllCarts();
            log.info("Lay danh sach gio hang thanh cong, so luong: {}", carts.size());

            ApiResponse<List<Cart>> response = new ApiResponse<>(
                    200,
                    "Lay danh sach gio hang thanh cong",
                    carts
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Loi khi lay danh sach gio hang: {}", e.getMessage());
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Lay gio hang theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable("id") Integer id) {
        try {
            log.info("Lay gio hang theo ID: {}", id);
            Cart cart = cartService.getCartById(id);
            log.info("Lay gio hang theo ID thanh cong");

            ApiResponse<Cart> response = new ApiResponse<>(
                    200,
                    "Lay gio hang thanh cong",
                    cart
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Loi khi lay gio hang theo ID {}: {}", id, e.getMessage());
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    "Khong tim thay gio hang",
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    // Tao gio hang moi cho user
    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CreateCartRequest request) {
        try {
            log.info("Tao gio hang moi cho user: {}", request.getUserId());
            Cart cart = cartService.createCartForUser(request.getUserId());
            log.info("Tao gio hang thanh cong cho user: {}", request.getUserId());

            ApiResponse<Cart> response = new ApiResponse<>(
                    200,
                    "Tao gio hang thanh cong",
                    cart
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Loi khi tao gio hang: {}", e.getMessage());
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Lay gio hang cua user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable("userId") Integer userId) {
        try {
            log.info("Lay gio hang cua user: {}", userId);
            Cart cart = cartService.getCartByUserId(userId);
            log.info("Lay gio hang cua user thanh cong");

            ApiResponse<Cart> response = new ApiResponse<>(
                    200,
                    "Lay gio hang thanh cong",
                    cart
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Loi khi lay gio hang cua user {}: {}", userId, e.getMessage());
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    "Khong tim thay gio hang",
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    // Xoa gio hang
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") Integer id) {
        try {
            log.info("Xoa gio hang: {}", id);
            cartService.deleteCart(id);
            log.info("Xoa gio hang thanh cong");

            ApiResponse<String> response = new ApiResponse<>(
                    200,
                    "Xoa gio hang thanh cong",
                    null
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Loi khi xoa gio hang {}: {}", id, e.getMessage());
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}