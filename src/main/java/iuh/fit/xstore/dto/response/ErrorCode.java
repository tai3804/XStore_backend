package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
//    === USER ===
    USER_NOT_FOUND(404, "User not found"),
    USER_EXISTED(409, "User already exists"),
//    OTP
    OTP_INVALID_OR_EXPIRATION(409, "Invalid OTP or had been expiration"),
    INVALID_INPUT(400, "Invalid input"),

//    === ACCOUNT ===
    ACCOUNT_NOT_FOUND(404, "Account not found"),
    ACCOUNT_EXISTED(409, "Account already exists"),
    USERNAME_NOT_FOUND(404, "Username not found"),
    USERNAME_EXISTED(409, "Username already exists"),
    PASSWORD_EMPTY(400, "Password cannot be empty"),
    INVALID_PASSWORD(401, "Invalid password"),
    INCORRECT_USERNAME_OR_PASSWORD(401, "Incorrect username or password"),
    ACCOUNT_DISABLED(403, "Your account has been disabled"),  // Sửa tên + thêm code + message
    ACCOUNT_LOCKED(403, "Your account is locked"),

//    === PRODUCT ===
    PRODUCT_NOT_FOUND(404, "Product not found"),
    PRODUCT_EXISTED(409, "Product already exists"),
    PRODUCT_TYPE_NOT_FOUND(404, "Product type not found"),
    PRODUCT_TYPE_EXISTED(409, "Product type already exists"),
    PRODUCT_INFO_NOT_FOUND(404, "Product info not found"),
    PRODUCT_INFO_EXISTED(409, "Product info already exists"),
    PRODUCT_INFO_REQUIRED(400, "Product info is required"),
    INVALID_PRODUCT_INFO(400, "Invalid product info"),

//    === ADDRESS ===
    ADDRESS_NOT_FOUND(404, "Address not found"),

//    === SHIP INFO ===
    SHIPINFO_NOT_FOUND(404, "Ship info not found"),

//    === FAVOURITE ===
    FAVOURITE_EXISTED(409, "Favorite already exists"),
    FAVOURITE_NOT_FOUND(404, "Favorite not found"),

    // ===== ORDER =====
    ORDER_NOT_FOUND(404, "Order not found"),
    ORDER_EXISTED(409, "Order already exists"),
    INVALID_QUANTITY(409, "Invalid quantity"),

    // ===== ORDER ITEM =====
    ORDER_ITEM_NOT_FOUND(404, "Order item not found"),
    ORDER_ITEM_EXISTED(409, "Order item already exists"),

    // ===== REQUEST =====
    REQUEST_NOT_FOUND(404, "Request not found"),
    REQUEST_ALREADY_EXISTS(409, "Request already exists for this order"),
    TOKEN_EXPIRED(403, "Token has expired"),

    // === Discount Errors ===
    DISCOUNT_NOT_FOUND(404, "Discount not found"),
    DISCOUNT_EXISTED(409, "Discount already exists"),

//    === Stock ===
    STOCK_NOT_FOUND(404, "Stock not found"),
    STOCK_ITEM_NOT_FOUND(404, "Stock item not found"),
    STOCK_DELETE_FAILED(409, "Cannot delete stock while items remain"),
    STOCK_IN_USE(409, "Cannot delete stock - it is being used in orders"),
    NOT_ENOUGH_QUANTITY(409, "Not enough quantity"),

    //cart
    CART_NOT_FOUND(404, "Cart not found"),
    CART_ITEM_NOT_FOUND(404, "Cart item not found"),
    CART_ITEM_EXISTED(409, "Cart item already exists"),

//    ===file===
    FILE_EMPTY(400, "File is empty"),
    FILE_UPLOAD_FAILED(400, "Could not upload file"),

//    ===payment===
    PAYMENT_FAILED(400, "Payment failed, please try again"),
    PAYMENT_PENDING(400, "Payment is pending"),
    INVALID_PAYMENT_METHOD(400, "Invalid payment method"),
    INSUFFICIENT_STOCK(400, "Insufficient stock"),
    INVALID_DISCOUNT(400, "Discount code is invalid or expired"),

//    ===validation===
    INVALID_REQUEST(400, "Invalid request"),
    INVALID_TOKEN(401, "Invalid token"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),

//    ===unknown===
    UNKNOWN_ERROR(500, "Something went wrong");

    private final int code;
    private final String message;
}
