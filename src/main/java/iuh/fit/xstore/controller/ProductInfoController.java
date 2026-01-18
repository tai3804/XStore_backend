package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.ProductInfo;
import iuh.fit.xstore.service.ProductInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;
import java.util.UUID;

/**
 * ProductInfoController - REST API cho ProductInfo
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductInfoController {
    
    private final ProductInfoService productInfoService;

    // Thu muc luu anh bien the
    private static final String UPLOAD_DIR = "uploads/products/";

    /**
     * Luu file anh bien the len server
     */
    private String saveProductInfoImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Tạo thư mục nếu chưa tồn tại
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Lấy tên file gốc và xóa spaces, thay bằng underscore
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            originalFilename = originalFilename.replaceAll("\\s+", "_");  // Xóa tất cả spaces
        } else {
            originalFilename = "image";
        }

        // Tạo tên file unique: productInfoImage_UUID_cleanedname
        String fileName = "productInfoImage_" + UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Lưu file
        Files.write(filePath, file.getBytes());

        // Return đường dẫn tương đối để frontend có thể access
        return "/uploads/products/" + fileName;
    }

    /**
     * GET /api/products/{productId}/info - Lấy tất cả product info của sản phẩm
     */
    @GetMapping("/{productId}/info")
    public ResponseEntity<?> getProductInfoByProductId(@PathVariable("productId") int productId) {
        try {
            log.info("Lay thong tin bien the san pham ID: {}", productId);
            List<ProductInfo> productInfoList = productInfoService.findByProductId(productId);
            log.info("Lay thong tin bien the san pham thanh cong, so luong: {}", productInfoList.size());
            ApiResponse<List<ProductInfo>> response = new ApiResponse<>(
                    200,
                    "Lấy thông tin biến thể sản phẩm thành công",
                    productInfoList
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * GET /api/products/{productId}/colors - Lấy danh sách màu sắc của sản phẩm
     */
    @GetMapping("/{productId}/colors")
    public ResponseEntity<?> getProductColors(@PathVariable("productId") int productId) {
        try {
            log.info("Lay danh sach mau sac san pham ID: {}", productId);
            List<Map<String, String>> colors = productInfoService.getDistinctColors(productId);
            log.info("Lay danh sach mau sac thanh cong, so luong: {}", colors.size());
            ApiResponse<List<Map<String, String>>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách màu sắc thành công",
                    colors
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * GET /api/products/{productId}/sizes - Lấy danh sách kích thước của sản phẩm
     */
    @GetMapping("/{productId}/sizes")
    public ResponseEntity<?> getProductSizes(@PathVariable("productId") int productId) {
        try {
            log.info("Lay danh sach kich thuoc san pham ID: {}", productId);
            List<String> sizes = productInfoService.getDistinctSizes(productId);
            log.info("Lay danh sach kich thuoc thanh cong, so luong: {}", sizes.size());
            ApiResponse<List<String>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách kích thước thành công",
                    sizes
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * GET /api/products/info/{id} - Lấy product info theo ID
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getProductInfoById(@PathVariable("id") int id) {
        try {
            log.info("Lay thong tin bien the theo ID: {}", id);
            ProductInfo productInfo = productInfoService.findById(id);
            log.info("Lay thong tin bien the thanh cong");
            ApiResponse<ProductInfo> response = new ApiResponse<>(
                    200,
                    "Lấy thông tin biến thể thành công",
                    productInfo
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * POST /api/products/{productId}/info/upload - Tạo product info với ảnh (multipart/form-data)
     */
    @PostMapping("/{productId}/info/upload")
    public ResponseEntity<?> createProductInfoWithImage(
            @PathVariable("productId") int productId,
            @RequestParam("colorName") String colorName,
            @RequestParam("colorHexCode") String colorHexCode,
            @RequestParam("sizeName") String sizeName,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            log.info("Tao bien the san pham ID {}, color: {}, size: {}", productId, colorName, sizeName);
            // Xu ly anh neu co
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = saveProductInfoImage(image);
                log.info("Anh bien the san pham duoc luu: {}", imagePath);
            }

            // Tạo ProductInfo object
            ProductInfo productInfo = new ProductInfo();
            productInfo.setColorName(colorName);
            productInfo.setColorHexCode(colorHexCode);
            productInfo.setSizeName(sizeName);
            productInfo.setImage(imagePath);

            ProductInfo created = productInfoService.create(productId, productInfo);
            log.info("Tao bien the san pham thanh cong");
            ApiResponse<ProductInfo> response = new ApiResponse<>(
                    201,
                    "Tạo biến thể sản phẩm thành công",
                    created
            );
            
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * POST /api/products/{productId}/info/batch/upload - Tạo nhiều product info với ảnh cùng lúc (multipart/form-data)
     */
    @PostMapping("/{productId}/info/batch/upload")
    public ResponseEntity<?> createMultipleProductInfoWithImages(
            @PathVariable("productId") int productId,
            @RequestParam("colorNames") List<String> colorNames,
            @RequestParam("colorHexCodes") List<String> colorHexCodes,
            @RequestParam("sizeNames") List<String> sizeNames,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        try {
            log.info("Tao nhieu bien the san pham ID {}, so luong: {}", productId, colorNames.size());
            List<ProductInfo> productInfoList = new ArrayList<>();
            
            for (int i = 0; i < colorNames.size(); i++) {
                ProductInfo info = new ProductInfo();
                info.setColorName(colorNames.get(i));
                info.setColorHexCode(colorHexCodes.get(i));
                info.setSizeName(sizeNames.get(i));
                
                // Xử lý ảnh nếu có
                if (images != null && i < images.size() && images.get(i) != null && !images.get(i).isEmpty()) {
                    String imagePath = saveProductInfoImage(images.get(i));
                    info.setImage(imagePath);
                }
                
                productInfoList.add(info);
            }

            List<ProductInfo> created = productInfoService.saveAll(productId, productInfoList);
            log.info("Tao nhieu bien the san pham thanh cong, so luong: {}", created.size());
            ApiResponse<List<ProductInfo>> response = new ApiResponse<>(
                    201,
                    "Tạo biến thể sản phẩm thành công",
                    created
            );
            
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * PUT /api/products/info/{id}/upload - Cập nhật product info với ảnh (multipart/form-data)
     */
    @PutMapping("/info/{id}/upload")
    public ResponseEntity<?> updateProductInfoWithImage(
            @PathVariable("id") int id,
            @RequestParam("colorName") String colorName,
            @RequestParam("colorHexCode") String colorHexCode,
            @RequestParam("sizeName") String sizeName,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            log.info("Cap nhat bien the san pham ID: {}", id);
            // Xu ly anh: neu co file moi, luu file moi; neu khong, giu anh cu
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = saveProductInfoImage(image);
                log.info("Anh bien the san pham moi duoc luu: {}", imagePath);
            } else {
                // Giu anh cu: fetch product info cu tu DB
                ProductInfo existingInfo = productInfoService.findById(id);
                imagePath = existingInfo.getImage();
                log.info("Giu anh cu cua bien the san pham");
            }

            // Tạo ProductInfo object với dữ liệu mới
            ProductInfo productInfo = new ProductInfo();
            productInfo.setColorName(colorName);
            productInfo.setColorHexCode(colorHexCode);
            productInfo.setSizeName(sizeName);
            productInfo.setImage(imagePath);

            ProductInfo updated = productInfoService.update(id, productInfo);
            log.info("Cap nhat bien the san pham thanh cong");
            ApiResponse<ProductInfo> response = new ApiResponse<>(
                    200,
                    "Cập nhật biến thể sản phẩm thành công",
                    updated
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

    /**
     * DELETE /api/products/info/{id} - Xóa product info
     */
    @DeleteMapping("/info/{id}")
    public ResponseEntity<?> deleteProductInfo(@PathVariable("id") int id) {
        try {
            log.info("Xoa bien the san pham ID: {}", id);
            productInfoService.delete(id);
            log.info("Xoa bien the san pham thanh cong");
            
            ApiResponse<Void> response = new ApiResponse<>(
                    200,
                    "Xóa biến thể sản phẩm thành công",
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

    /**
     * DELETE /api/products/{productId}/info - Xóa tất cả product info của sản phẩm
     */
    @DeleteMapping("/{productId}/info")
    public ResponseEntity<?> deleteAllProductInfo(@PathVariable("productId") int productId) {
        try {
            log.info("Xoa tat ca bien the san pham ID: {}", productId);
            productInfoService.deleteByProductId(productId);
            log.info("Xoa tat ca bien the san pham thanh cong");
            
            ApiResponse<Void> response = new ApiResponse<>(
                    200,
                    "Xóa tất cả biến thể sản phẩm thành công",
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
