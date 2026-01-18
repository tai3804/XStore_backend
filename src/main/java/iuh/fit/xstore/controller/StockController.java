package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.dto.response.StockProductResponse;
import iuh.fit.xstore.model.Stock;
import iuh.fit.xstore.model.StockItem;
import iuh.fit.xstore.service.StockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/stocks")
@AllArgsConstructor
public class StockController {

    private final StockService stockService;

    // Stock
    @GetMapping
    ApiResponse<List<Stock>> getStocks() {
        log.info("Lay danh sach tat ca kho hang");
        List<Stock> stocks = stockService.findAll();
        log.info("Lay danh sach kho hang thanh cong, so luong: {}", stocks.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, stocks);
    }

    @GetMapping("/{id}")
    ApiResponse<Stock> getStock(@PathVariable("id") int id) {
        log.info("Lay kho hang theo ID: {}", id);
        Stock stock = stockService.findById(id);
        log.info("Lay kho hang thanh cong");
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, stock);
    }

    @PostMapping
    ApiResponse<Stock> createStock(@RequestBody Stock stock) {
        log.info("Tao kho hang moi: {}", stock.getName());
        Stock createdStock = stockService.create(stock);
        log.info("Tao kho hang thanh cong, ID: {}", createdStock.getId());
        return new ApiResponse<>(SuccessCode.STOCK_CREATED, createdStock);
    }

    @PutMapping("/{id}")
    ApiResponse<Stock> updateStock(@PathVariable("id") int id, @RequestBody Stock stock) {
        log.info("Cap nhat kho hang ID: {}", id);
        Stock updatedStock = stockService.update(id, stock);
        log.info("Cap nhat kho hang thanh cong");
        return new ApiResponse<>(SuccessCode.STOCK_UPDATED, updatedStock);
    }

    @DeleteMapping("/{id}")
    ApiResponse<Integer> deleteStock(@PathVariable("id") int id) {
        log.info("Xoa kho hang ID: {}", id);
        Integer deletedId = stockService.delete(id);
        log.info("Xoa kho hang thanh cong");
        return new ApiResponse<>(SuccessCode.STOCK_DELETED, deletedId);
    }

    // Xem danh sách sản phẩm trong kho
    @GetMapping("/{id}/items")
    ApiResponse<List<StockProductResponse>> getItems(@PathVariable("id") int id) {
        log.info("Lay danh sach san pham trong kho ID: {}", id);
        List<StockProductResponse> items = stockService.getItemsOfStock(id);
        log.info("Lay danh sach san pham trong kho thanh cong, so luong: {}", items.size());
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, items);
    }

    // Tạo mới hoặc cập nhật số lượng
    @PostMapping("/{id}/items")
    ApiResponse<StockItem> setItemQuantity(@PathVariable("id") int id,
                                           @RequestParam("productInfoId") int productInfoId,
                                           @RequestParam("quantity") int quantity) {
        log.info("Dat so luong san pham trong kho ID {}, productInfoId: {}, quantity: {}", id, productInfoId, quantity);
        StockItem stockItem = stockService.setItemQuantity(id, productInfoId, quantity);
        log.info("Dat so luong san pham thanh cong");
        return new ApiResponse<>(SuccessCode.STOCK_ITEM_UPDATED, stockItem);
    }

    // Tăng số lượng
    @PostMapping("/{id}/items/increase")
    ApiResponse<StockItem> increaseItemQuantity(@PathVariable("id") int id,
                                                @RequestParam("productInfoId") int productInfoId,
                                                @RequestParam("amount") int amount) {
        log.info("Tang so luong san pham trong kho ID {}, productInfoId: {}, amount: {}", id, productInfoId, amount);
        StockItem stockItem = stockService.increaseItemQuantity(id, productInfoId, amount);
        log.info("Tang so luong san pham thanh cong");
        return new ApiResponse<>(SuccessCode.STOCK_ITEM_UPDATED, stockItem);
    }

    // Giảm số lượng
    @PostMapping("/{id}/items/decrease")
    ApiResponse<StockItem> decreaseItemQuantity(@PathVariable("id") int id,
                                                @RequestParam("productInfoId") int productInfoId,
                                                @RequestParam("amount") int amount) {
        log.info("Giam so luong san pham trong kho ID {}, productInfoId: {}, amount: {}", id, productInfoId, amount);
        StockItem stockItem = stockService.decreaseItemQuantity(id, productInfoId, amount);
        log.info("Giam so luong san pham thanh cong");
        return new ApiResponse<>(SuccessCode.STOCK_ITEM_UPDATED, stockItem);
    }

    // Xóa sản phẩm khỏi kho
    @DeleteMapping("/{id}/items")
    ApiResponse<Void> deleteItem(@PathVariable("id") int id,
                                 @RequestParam("productInfoId") int productInfoId) {
        log.info("Xoa san pham khoi kho ID {}, productInfoId: {}", id, productInfoId);
        stockService.deleteItem(id, productInfoId);
        log.info("Xoa san pham khoi kho thanh cong");
        return new ApiResponse<>(SuccessCode.STOCK_ITEM_DELETED, null);
    }

    // Lấy tổng số lượng của tất cả variants của product từ tất cả kho
    @GetMapping("/products/{productId}/total-quantities")
    ApiResponse<Map<Integer, Integer>> getTotalQuantitiesForProduct(@PathVariable("productId") int productId) {
        log.info("Lay tong so luong san pham ID: {}", productId);
        Map<Integer, Integer> quantities = stockService.getTotalQuantitiesForProduct(productId);
        log.info("Lay tong so luong san pham thanh cong");
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, quantities);
    }
}
