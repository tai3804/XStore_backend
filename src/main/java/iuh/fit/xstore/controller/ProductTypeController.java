package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.ProductType;
import iuh.fit.xstore.service.ProductTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/product-types")
@AllArgsConstructor
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    @GetMapping
    public ApiResponse<List<ProductType>> getAllProductTypes() {
        log.info("Lay danh sach tat ca loai san pham");
        List<ProductType> productTypes = productTypeService.findAll();
        log.info("Lay danh sach loai san pham thanh cong, so luong: {}", productTypes.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, productTypes);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductType> getProductTypeById(@PathVariable("id") int id) {
        log.info("Lay loai san pham theo ID: {}", id);
        ProductType productType = productTypeService.findById(id);
        log.info("Lay loai san pham thanh cong");
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, productType);
    }

    @PostMapping
    public ApiResponse<ProductType> createProductType(@RequestBody ProductType productType) {
        log.info("Tao loai san pham moi: {}", productType.getName());
        ProductType createdProductType = productTypeService.createProductType(productType);
        log.info("Tao loai san pham thanh cong, ID: {}", createdProductType.getId());
        return new ApiResponse<>(SuccessCode.PRODUCT_TYPE_CREATED, createdProductType);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductType> updateProductType(@PathVariable("id") int id, @RequestBody ProductType productType) {
        log.info("Cap nhat loai san pham ID: {}", id);
        productType.setId(id);
        ProductType updatedProductType = productTypeService.updateProductType(id, productType);
        log.info("Cap nhat loai san pham thanh cong");
        return new ApiResponse<>(SuccessCode.PRODUCT_TYPE_UPDATED, updatedProductType);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Integer> deleteProductType(@PathVariable("id") int id) {
        log.info("Xoa loai san pham ID: {}", id);
        productTypeService.deleteProductType(id);
        log.info("Xoa loai san pham thanh cong");
        return new ApiResponse<>(SuccessCode.PRODUCT_TYPE_DELETED, id);
    }
}