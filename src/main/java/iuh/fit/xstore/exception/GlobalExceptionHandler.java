package iuh.fit.xstore.exception;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Object>> handleRuntime(RuntimeException ex) {
        System.err.println("RuntimeException: " + ex.getMessage());
        ex.printStackTrace();
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(400);
        response.setMessage(ex.getMessage());
        response.setResult(null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        System.err.println("JSON Deserialization Error: " + ex.getMessage());
        ex.printStackTrace();
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(400);
        response.setMessage("Invalid JSON format: " + ex.getMostSpecificCause().getMessage());
        response.setResult(null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse<Object>> handleAppException(AppException appException) {
        System.err.println("AppException: " + appException.getMessage());
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(appException.getErrorCode().getCode());
        response.setMessage(appException.getErrorCode().getMessage());
        response.setResult(null);

        return ResponseEntity.status(appException.getErrorCode().getCode()).body(response);
    }

}
