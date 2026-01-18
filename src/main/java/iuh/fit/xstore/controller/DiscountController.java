package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Discount;
import iuh.fit.xstore.service.DiscountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/discounts")
@AllArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping
    ApiResponse<List<Discount>> getDiscounts() {
        log.info("Lay danh sach tat ca giam gia");
        List<Discount> discounts = discountService.findAll();
        log.info("Lay danh sach giam gia thanh cong, so luong: {}", discounts.size());
        System.out.println("Fetching discounts, first discount isActive: " + 
            (discounts.isEmpty() ? "N/A" : discounts.get(0).getIsActive()));
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, discounts);
    }

    @GetMapping("/{id}")
    public ApiResponse<Discount> getDiscount(@PathVariable("id") int id) {
        log.info("Lay giam gia theo ID: {}", id);
        Discount discount = discountService.findById(id);
        log.info("Lay giam gia thanh cong");
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, discount);
    }

    @PostMapping
    ApiResponse<Discount> createDiscount(@RequestBody Discount discount) {
        log.info("Tao giam gia moi: {}", discount.getName());
        Discount createdDiscount = discountService.createDiscount(discount);
        log.info("Tao giam gia thanh cong, ID: {}", createdDiscount.getId());
        return new ApiResponse<>(SuccessCode.DISCOUNT_CREATED, createdDiscount);
    }

    @PutMapping("/{id}")
    ApiResponse<Discount> updateDiscount(@PathVariable("id") int id, @RequestBody Discount discount) {
        log.info("Cap nhat giam gia ID: {}", id);
        discount.setId(id);
        Discount updatedDiscount = discountService.updateDiscount(discount);
        log.info("Cap nhat giam gia thanh cong");
        return new ApiResponse<>(SuccessCode.DISCOUNT_UPDATED, updatedDiscount);
    }

    @DeleteMapping("/{id}")
    ApiResponse<Integer> deleteDiscount(@PathVariable("id") int id) {
        log.info("Xoa giam gia ID: {}", id);
        discountService.deleteDiscount(id);
        log.info("Xoa giam gia thanh cong");
        return new ApiResponse<>(SuccessCode.DISCOUNT_DELETED, id);
    }
}
