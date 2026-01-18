package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.AddToCartRequest;
import iuh.fit.xstore.dto.request.UpdateCartItemRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.CartItem;
import iuh.fit.xstore.service.CartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    // Lay tat ca cart items
    @GetMapping
    public ResponseEntity<?> getAllCartItems() {
        try {
            log.info("Lay danh sach tat ca cart items");
            List<CartItem> cartItems = cartItemService.getAllCartItems();
            log.info("Lay danh sach cart items thanh cong, so luong: {}", cartItems.size());

            ApiResponse<List<CartItem>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách cart items thành công",
                    cartItems
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Lay cart items theo cart ID
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<?> getCartItemsByCartId(@PathVariable("cartId") Integer cartId) {
        try {
            log.info("Lay danh sach cart items theo cartId: {}", cartId);
            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cartId);
            log.info("Lay danh sach cart items thanh cong, so luong: {}", cartItems.size());

            ApiResponse<List<CartItem>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách cart items thành công",
                    cartItems
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    "Không tìm thấy cart items",
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    // Lay cart item theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartItemById(@PathVariable("id") Integer id) {
        try {
            log.info("Lay cart item theo ID: {}", id);
            CartItem cartItem = cartItemService.getCartItemById(id);
            log.info("Lay cart item thanh cong");

            ApiResponse<CartItem> response = new ApiResponse<>(
                    200,
                    "Lấy cart item thành công",
                    cartItem
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    "Không tìm thấy cart item",
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        try {
            log.info("Them san pham vao gio hang, cartId: {}, productId: {}, quantity: {}", 
                     request.getCartId(), request.getProductId(), request.getQuantity());
            CartItem cartItem = cartItemService.addToCart(
                    request.getCartId(),
                    request.getProductId(),
                    request.getStockId(),
                    request.getQuantity(),
                    request.getProductInfoId()
            );

            log.info("Them san pham vao gio hang thanh cong");
            ApiResponse<CartItem> response = new ApiResponse<>(
                    200,
                    "Thêm sản phẩm vào giỏ hàng thành công",
                    cartItem
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable("id") Integer id,
            @RequestBody UpdateCartItemRequest request
    ) {
        try {
            log.info("Cap nhat so luong cart item ID: {}, quantity: {}", id, request.getQuantity());
            CartItem cartItem = cartItemService.updateQuantity(id, request.getQuantity());
            log.info("Cap nhat so luong thanh cong");

            ApiResponse<CartItem> response = new ApiResponse<>(
                    200,
                    "Cập nhật số lượng thành công",
                    cartItem
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable("id") Integer id) {
        try {
            log.info("Xoa san pham khoi gio hang ID: {}", id);
            cartItemService.removeFromCart(id);
            log.info("Xoa san pham khoi gio hang thanh cong");

            ApiResponse<String> response = new ApiResponse<>(
                    200,
                    "Xóa sản phẩm khỏi giỏ hàng thành công",
                    null
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}