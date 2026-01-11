package iuh.fit.xstore.controller;

import iuh.fit.xstore.config.VNPayConfig;
import iuh.fit.xstore.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // 1. API Tạo URL thanh toán
    @PostMapping("/create_payment")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            // Lấy các thông số từ Config
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TxnRef = String.valueOf(requestBody.get("orderId")); // Dùng Order ID làm mã giao dịch
            String vnp_IpAddr = VNPayConfig.getIpAddress(request);
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
            String vnp_OrderInfo = "Thanh toan don hang " + vnp_TxnRef;

            // Xử lý số tiền (VNPay yêu cầu nhân 100)
            long amount = Long.parseLong(String.valueOf(requestBody.get("amount"))) * 100;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");

            String bankCode = (String) requestBody.get("bankCode");
            if (bankCode != null && !bankCode.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bankCode);
            }

            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");

            // QUAN TRỌNG: URL này phải khớp với cấu hình trong application.properties
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_Returnurl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Tạo chuỗi hash và URL
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

            Map<String, Object> result = new HashMap<>();
            result.put("code", "00");
            result.put("message", "success");
            result.put("result", paymentUrl); // Frontend dùng key 'result' để lấy link

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", "99");
            error.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 2. API Xử lý kết quả trả về (Return URL)
    @GetMapping("/vnpay-return")
    public RedirectView vnpayReturn(HttpServletRequest request) {
        // Lấy tất cả tham số trả về từ VNPay
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        // Loại bỏ field hash để tính lại checksum
        if (fields.containsKey("vnp_SecureHashType")) fields.remove("vnp_SecureHashType");
        if (fields.containsKey("vnp_SecureHash")) fields.remove("vnp_SecureHash");

        // Cần thêm hàm hashAllFields vào VNPayConfig nếu chưa có (xem Bước 3)
        // Hoặc copy logic hash từ createPayment vào đây nếu lười sửa Config
        String signValue = hashAllFields(fields);

        String orderId = request.getParameter("vnp_TxnRef");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");

        // Địa chỉ Frontend để chuyển hướng về (Chạy Vite thường là 5173)
        String frontendUrl = "http://localhost:5173/payment/result";

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(transactionStatus)) {
                // Thanh toán thành công -> Cập nhật DB
                paymentService.confirmVNPayPayment(Integer.parseInt(orderId));

                return new RedirectView(frontendUrl + "?status=success&orderId=" + orderId);
            } else {
                // Thanh toán thất bại / Hủy
                return new RedirectView(frontendUrl + "?status=failed&orderId=" + orderId);
            }
        } else {
            // Sai chữ ký (Có thể do hack/man-in-middle)
            return new RedirectView(frontendUrl + "?status=error&message=InvalidSignature");
        }
    }

    // Helper method để hash (nên đưa vào VNPayConfig.java thì tốt hơn)
    private String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, sb.toString());
    }
}