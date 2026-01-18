package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.ProductFilterRequest;
import iuh.fit.xstore.dto.request.ProductCreateRequest;
import iuh.fit.xstore.dto.request.ProductUpdateRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductInfo;
import iuh.fit.xstore.model.ProductType;
import iuh.fit.xstore.service.ProductService;
import iuh.fit.xstore.service.ProductTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Thu muc luu anh
    private static final String UPLOAD_DIR = "uploads/products/";

    /**
     * Luu file anh len server
     */
    private String saveProductImage(MultipartFile file) throws Exception {
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

        // Tạo tên file unique: productImage_UUID_cleanedname
        String fileName = "productImage_" + UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Lưu file
        Files.write(filePath, file.getBytes());

        // Return đường dẫn tương đối để frontend có thể access
        return "/uploads/products/" + fileName;
    }

    /**
     * Tao san pham moi voi file anh (multipart/form-data)
     * POST /api/products/upload
     * NOTE: ProductInfo (colors, sizes, quantities) se duoc quan ly rieng qua ProductInfoController
     */
    @PostMapping("/upload")
    public ApiResponse<Product> createProductWithImage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("brand") String brand,
            @RequestParam("fabric") String fabric,
            @RequestParam("price") Double price,
            @RequestParam("priceInStock") Double priceInStock,
            @RequestParam("typeId") int typeId,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws Exception {
        log.info("Nhan du lieu san pham: name={}, brand={}, price={}, typeId={}", name, brand, price, typeId);

        // Luu anh neu co
        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            imagePath = saveProductImage(image);
            log.info("Anh san pham duoc luu: {}", imagePath);
        }

        // Chuyen DTO thanh Product entity
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setImage(imagePath);
        product.setBrand(brand);
        product.setFabric(fabric);
        product.setPrice(price);
        product.setPriceInStock(priceInStock);

        // Lay ProductType tu ID
        if (typeId > 0) {
            ProductType type = productTypeService.findById(typeId);
            product.setType(type);
        }

        // ProductInfo (colors, sizes, quantities) se duoc them sau qua ProductInfoController
        log.info("Tao san pham voi name: {}", name);
        Product createdProduct = productService.createProduct(product);
        log.info("Tao san pham thanh cong, ID: {}", createdProduct.getId());
        return new ApiResponse<>(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    /**
     * Cap nhat san pham voi file anh moi (multipart/form-data)
     * PUT /api/products/{id}/upload
     * NOTE: ProductInfo (colors, sizes, quantities) se duoc quan ly rieng qua ProductInfoController
     */
    @PutMapping("/{id}/upload")
    public ApiResponse<Product> updateProductWithImage(
            @PathVariable("id") int id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("brand") String brand,
            @RequestParam("fabric") String fabric,
            @RequestParam("price") Double price,
            @RequestParam("priceInStock") Double priceInStock,
            @RequestParam("typeId") int typeId,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws Exception {
        log.info("Cap nhat san pham ID: {}, name: {}", id, name);

        // Chuyen DTO thanh Product entity
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setBrand(brand);
        product.setFabric(fabric);
        product.setPrice(price);
        product.setPriceInStock(priceInStock);

        // Xu ly anh: neu co file moi, luu file moi; neu khong, giu anh cu
        if (image != null && !image.isEmpty()) {
            String newImagePath = saveProductImage(image);
            product.setImage(newImagePath);
            log.info("Anh san pham moi duoc luu: {}", newImagePath);
        } else {
            // Giu anh cu: fetch product cu tu DB
            Product existingProduct = productService.findById(id);
            product.setImage(existingProduct.getImage());
            log.info("Giu anh cu cua san pham");
        }

        // Lay ProductType tu ID
        if (typeId > 0) {
            ProductType type = productTypeService.findById(typeId);
            product.setType(type);
        }

        // ProductInfo (colors, sizes, quantities) se duoc cap nhat rieng qua ProductInfoController
        Product updatedProduct = productService.updateProduct(product);
        log.info("San pham ID {} duoc cap nhat thanh cong", id);
        return new ApiResponse<>(SuccessCode.PRODUCT_UPDATED, updatedProduct);
    }

    @GetMapping
    public ApiResponse<List<Product>> getAllProducts() {
        log.info("Lay danh sach tat ca san pham");
        List<Product> products = productService.findAll();
        log.info("Lay danh sach san pham thanh cong, so luong: {}", products.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, products);
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Backend is working! Products controller is accessible.";
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> getProductById(@PathVariable("id") int id) {
        log.info("Lay san pham theo ID: {}", id);
        Product product = productService.findById(id);
        log.info("Lay san pham thanh cong");
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, product);
    }

    @GetMapping("/type/{typeId}")
    public ApiResponse<List<Product>> getProductsByTypeId(@PathVariable("typeId") int typeId) {
        log.info("Lay san pham theo loai typeId: {}", typeId);
        List<Product> products = productService.findByTypeId(typeId);
        log.info("Lay danh sach san pham theo loai thanh cong, so luong: {}", products.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, products);
    }

    @PostMapping
    public ApiResponse<Product> createProduct(@RequestBody ProductCreateRequest request) {
        log.info("Tao san pham moi: {}", request.getName());
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setBrand(request.getBrand());
        product.setFabric(request.getFabric());
        product.setPrice(request.getPrice());
        product.setPriceInStock(request.getPriceInStock());

        if (request.getTypeId() > 0) {
            product.setType(productTypeService.findById(request.getTypeId()));
        }

        // --- Thêm phần ProductInfos ---
        if (request.getProductInfos() != null && !request.getProductInfos().isEmpty()) {
            List<ProductInfo> infos = request.getProductInfos().stream().map(infoReq -> {
                ProductInfo info = new ProductInfo();
                info.setColorName(infoReq.getColorName());
                info.setColorHexCode(infoReq.getColorHexCode());
                info.setSizeName(infoReq.getSizeName());
                info.setImage(infoReq.getImage());
                info.setProduct(product); // quan trọng: gán product để cascade lưu
                return info;
            }).toList();
            product.setProductInfos(infos);
        }

        Product createdProduct = productService.createProduct(product);
        log.info("Tao san pham thanh cong, ID: {}", createdProduct.getId());
        return new ApiResponse<>(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable("id") int id, @RequestBody ProductUpdateRequest request) {
        log.info("Cap nhat san pham ID: {}", id);
        try {
            Product product = new Product();
            product.setId(id);
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setImage(request.getImage());
            product.setBrand(request.getBrand());
            product.setFabric(request.getFabric());
            product.setPrice(request.getPrice());
            product.setPriceInStock(request.getPriceInStock());

            if (request.getTypeId() > 0) {
                ProductType type = productTypeService.findById(request.getTypeId());
                product.setType(type);
            }

            if (request.getProductInfos() != null && !request.getProductInfos().isEmpty()) {
                request.getProductInfos().forEach(info -> info.setProduct(product)); // quan trong de cascade luu
                product.setProductInfos(request.getProductInfos());
            }

            log.info("Chuan bi entity san pham: {}", product.getName());
            Product updatedProduct = productService.updateProduct(product);
            log.info("San pham duoc cap nhat thanh cong");
            return new ApiResponse<>(SuccessCode.PRODUCT_UPDATED, updatedProduct);
        } catch (Exception e) {
            log.error("Loi cap nhat san pham ID {}: {}", id, e.getMessage());
            throw e;
        }
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Integer> deleteProduct(@PathVariable("id") int id) {
        log.info("Xoa san pham ID: {}", id);
        productService.deleteProduct(id);
        log.info("Xoa san pham thanh cong");
        return new ApiResponse<>(SuccessCode.PRODUCT_DELETED, id);
    }

    @GetMapping("/search")
    public ApiResponse<List<Product>> searchProducts(@RequestParam(value = "q", required = false) String keyword) {
        log.info("Tim kiem san pham voi tu khoa: {}", keyword);
        List<Product> products = productService.searchProducts(keyword);
        log.info("Tim kiem san pham thanh cong, so luong: {}", products.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, products);
    }

    @GetMapping("/{id}/stocks")
    public ApiResponse<List<Object>> getProductStocks(@PathVariable("id") int id) {
        log.info("Lay danh sach kho hang cua san pham ID: {}", id);
        List<Object> stockItems = productService.getProductStocks(id);
        log.info("Lay danh sach kho hang thanh cong, so luong: {}", stockItems.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, stockItems);
    }

    // Colors và Sizes giờ được quản lý qua ProductInfoController
    // Xem ProductInfoController để biết thêm chi tiết

    /**
     * Filter products with advanced criteria
     * POST /api/products/filter
     * Body: ProductFilterRequest JSON
     */
    @PostMapping("/filter")
    public ApiResponse<List<Product>> filterProducts(@RequestBody ProductFilterRequest filterRequest) {
        log.info("Loc san pham voi dieu kien loc");
        List<Product> filteredProducts = productService.filterProducts(filterRequest);
        log.info("Loc san pham thanh cong, so luong: {}", filteredProducts.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, filteredProducts);
    }

    /**
     * Filter products with query parameters (alternative GET method)
     * GET /api/products/filter?productTypeId=1&minPrice=100000&maxPrice=500000&sortBy=price-asc
     */
    @GetMapping("/filter")
    public ApiResponse<List<Product>> filterProductsWithParams(
            @RequestParam(required = false) Integer productTypeId,
            @RequestParam(required = false) String productTypeName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy
    ) {
        log.info("Loc san pham voi tham so: typeId={}, typeName={}, minPrice={}, maxPrice={}, sortBy={}", 
                 productTypeId, productTypeName, minPrice, maxPrice, sortBy);
        ProductFilterRequest filterRequest = new ProductFilterRequest();
        filterRequest.setProductTypeId(productTypeId);
        filterRequest.setProductTypeName(productTypeName);
        filterRequest.setMinPrice(minPrice);
        filterRequest.setMaxPrice(maxPrice);
        filterRequest.setSortBy(sortBy);

        List<Product> filteredProducts = productService.filterProducts(filterRequest);
        log.info("Loc san pham thanh cong, so luong: {}", filteredProducts.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, filteredProducts);
    }
}
