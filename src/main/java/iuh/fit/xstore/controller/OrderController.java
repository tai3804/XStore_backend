package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.CheckoutRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Order;
import iuh.fit.xstore.model.OrderItem;
import iuh.fit.xstore.service.OrderService;
import iuh.fit.xstore.service.PaymentService;
import iuh.fit.xstore.service.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PdfService pdfService;

    // ========== ORDER ==========
    @GetMapping
    public ApiResponse<List<Order>> getAllOrders() {
        log.info("Lay danh sach tat ca don hang");
        var orders = orderService.findAllOrders();
        log.info("Lay danh sach don hang thanh cong, so luong: {}", orders.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), orders);
    }

    /**
     * Lay don hang theo ID
     * GET /api/orders/{id}
     * @param id ID don hang
     * @return ApiResponse thong tin don hang hoac ORDER_NOT_FOUND
     */
    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderById(@PathVariable("id") int id) {
        log.info("Lay don hang theo ID: {}", id);
        var order = orderService.findOrderById(id);
        if (order == null) {
            log.warn("Don hang ID {} khong tim thay", id);
            return new ApiResponse<>(ErrorCode.ORDER_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_NOT_FOUND.getMessage(), null);
        }
        log.info("Lay don hang ID {} thanh cong", id);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), order);
    }

    @PostMapping
    public ApiResponse<Order> createOrder(@RequestBody Order order) {
        log.info("Tao don hang moi");
        var created = orderService.createOrder(order);
        log.info("Tao don hang thanh cong, ID: {}", created.getId());
        return new ApiResponse<>(SuccessCode.ORDER_CREATED.getCode(),
                SuccessCode.ORDER_CREATED.getMessage(), created);
    }

    /**
     * Cap nhat trang thai don hang
     * PUT /api/orders/{id}/status
     * @param id ID don hang
     * @param status trang thai moi (JSON string)
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Order> updateOrderStatus(@PathVariable("id") int id, @RequestBody String status) {
        log.info("Cap nhat trang thai don hang ID: {}, trang thai: {}", id, status.replace("\"", ""));
        var updated = orderService.updateOrderStatus(id, status.replace("\"", "")); // Remove quotes from JSON string
        if (updated == null) {
            log.warn("Don hang ID {} khong tim thay", id);
            return new ApiResponse<>(ErrorCode.ORDER_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_NOT_FOUND.getMessage(), null);
        }
        log.info("Cap nhat trang thai don hang ID {} thanh cong", id);
        return new ApiResponse<>(SuccessCode.ORDER_UPDATED.getCode(),
                SuccessCode.ORDER_UPDATED.getMessage(), updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Integer> deleteOrder(@PathVariable("id") int id) {
        log.info("Xoa don hang ID: {}", id);
        var deletedId = orderService.deleteOrder(id);
        log.info("Xoa don hang thanh cong");
        return new ApiResponse<>(SuccessCode.ORDER_DELETED.getCode(),
                SuccessCode.ORDER_DELETED.getMessage(), deletedId);
    }

    // ========== ORDER ITEM ==========
    @GetMapping("/items")
    public ApiResponse<List<OrderItem>> getAllOrderItems() {
        log.info("Lay danh sach tat ca don hang item");
        var items = orderService.findAllOrderItems();
        log.info("Lay danh sach order item thanh cong, so luong: {}", items.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), items);
    }

    /**
     * Lay order item theo ID
     * GET /api/orders/items/{id}
     */
    @GetMapping("/items/{id}")
    public ApiResponse<OrderItem> getOrderItemById(@PathVariable("id") int id) {
        log.info("Lay order item theo ID: {}", id);
        var item = orderService.findOrderItemById(id);
        if (item == null) {
            log.warn("Order item ID {} khong tim thay", id);
            return new ApiResponse<>(ErrorCode.ORDER_ITEM_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_ITEM_NOT_FOUND.getMessage(), null);
        }
        log.info("Lay order item ID {} thanh cong", id);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), item);
    }

    @PostMapping("/items")
    public ApiResponse<OrderItem> createOrderItem(@RequestBody OrderItem item) {
        log.info("Tao order item moi");
        var created = orderService.createOrderItem(item);
        log.info("Tao order item thanh cong, ID: {}", created.getId());
        return new ApiResponse<>(SuccessCode.ORDER_ITEM_CREATED.getCode(),
                SuccessCode.ORDER_ITEM_CREATED.getMessage(), created);
    }

    /**
     * Cap nhat order item
     * PUT /api/orders/items/{id}
     */
    @PutMapping("/items/{id}")
    public ApiResponse<OrderItem> updateOrderItem(@PathVariable("id") int id, @RequestBody OrderItem item) {
        log.info("Cap nhat order item ID: {}", id);
        var updated = orderService.updateOrderItem(id, item);
        if (updated == null) {
            log.warn("Order item ID {} khong tim thay", id);
            return new ApiResponse<>(ErrorCode.ORDER_ITEM_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_ITEM_NOT_FOUND.getMessage(), null);
        }
        log.info("Cap nhat order item ID {} thanh cong", id);
        return new ApiResponse<>(SuccessCode.ORDER_ITEM_UPDATED.getCode(),
                SuccessCode.ORDER_ITEM_UPDATED.getMessage(), updated);
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<Integer> deleteOrderItem(@PathVariable("id") int id) {
        log.info("Xoa order item ID: {}", id);
        var deletedId = orderService.deleteOrderItem(id);
        log.info("Xoa order item thanh cong");
        return new ApiResponse<>(SuccessCode.ORDER_ITEM_DELETED.getCode(),
                SuccessCode.ORDER_ITEM_DELETED.getMessage(), deletedId);
    }

    // ========== CHECKOUT & PAYMENT ==========
    @PostMapping("/checkout")
    public ApiResponse<Order> checkout(@RequestBody CheckoutRequest request) {
        log.info("Xu ly thanh toan voi phuong thuc: {}", request.getPaymentMethod());
        Order order = paymentService.processCheckout(request);
        
        // Xu ly thanh toan
        if (paymentService.processPayment(order, request.getPaymentMethod())) {
            log.info("Thanh toan thanh cong! Don hang ID: {}", order.getId());
            return new ApiResponse<>(SuccessCode.ORDER_CREATED.getCode(),
                    "Thanh toan thanh cong! Don hang da duoc tao.", order);
        } else {
            log.error("Thanh toan that bai cho don hang ID: {}", order.getId());
            return new ApiResponse<>(ErrorCode.PAYMENT_FAILED.getCode(),
                    "Thanh toán thất bại. Vui lòng thử lại.", null);
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ApiResponse<String> cancelOrder(
            @PathVariable("orderId") int orderId,
            @RequestParam(defaultValue = "Khach hang yeu cau") String reason) {
        log.info("Huy don hang ID: {}, ly do: {}", orderId, reason);
        paymentService.cancelOrder(orderId, reason);
        log.info("Huy don hang thanh cong");
        return new ApiResponse<>(SuccessCode.ORDER_DELETED.getCode(),
                "Đơn hàng đã được hủy.", "success");
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Order>> getUserOrders(@PathVariable("userId") int userId) {
        log.info("Lay don hang cua user ID: {}", userId);
        List<Order> orders = paymentService.getUserOrders(userId);
        log.info("Lay danh sach don hang cua user thanh cong, so luong: {}", orders.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), orders);
    }

    // ========== PDF EXPORT ==========
    @GetMapping("/{id}/pdf")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadOrderPdf(@PathVariable("id") int id, Authentication authentication) {
        try {
            log.info("Tai PDF don hang ID: {}", id);
            // Kiem tra don hang ton tai
            var order = orderService.findOrderById(id);
            if (order == null) {
                log.warn("Don hang ID {} khong tim thay", id);
                return ResponseEntity.notFound().build();
            }

            // Kiểm tra quyền truy cập - chỉ chủ đơn hàng hoặc admin mới có thể tải PDF
            String currentUsername = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            boolean isOrderOwner = order.getUser() != null &&
                    order.getUser().getAccount().getUsername() != null &&
                    order.getUser().getAccount().getUsername().equals(currentUsername);

            if (!isAdmin && !isOrderOwner) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            // Tao PDF
            byte[] pdfBytes = pdfService.generateOrderPdf(order);

            if (pdfBytes == null || pdfBytes.length == 0) {
                log.error("Khong tao duoc PDF cho don hang ID: {}", id);
                return ResponseEntity.internalServerError().build();
            }

            log.info("Tao PDF don hang ID {} thanh cong", id);

            // Thiết lập headers cho file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "don-hang-" + id + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (IllegalArgumentException e) {
            // Loi tham so khong hop le
            log.error("Tham so khong hop le khi tao PDF cho don hang ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log loi va tra ve loi server
            log.error("Loi khi tao PDF cho don hang ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}