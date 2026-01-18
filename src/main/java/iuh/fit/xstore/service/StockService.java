package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.StockProductResponse;
import iuh.fit.xstore.dto.response.StockProductInfoResponse;
import iuh.fit.xstore.model.Address;
import iuh.fit.xstore.model.ProductInfo;
import iuh.fit.xstore.model.Stock;
import iuh.fit.xstore.model.StockItem;
import iuh.fit.xstore.repository.ProductInfoRepository;
import iuh.fit.xstore.repository.StockItemRepository;
import iuh.fit.xstore.repository.StockRepository;
import iuh.fit.xstore.repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import iuh.fit.xstore.repository.AddressRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockService {
    private final StockRepository stockRepo;
    private final StockItemRepository stockItemRepo;
    private final ProductInfoRepository productInfoRepo;
    private final AddressRepository addressRepo;
    private final OrderItemRepository orderItemRepo;

    //Stock
    public List<Stock> findAll() {
        return stockRepo.findAll();
    }

    public Stock findById(int id) {
        return stockRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STOCK_NOT_FOUND));
    }

    public Stock create(Stock stock) {
        // Persist address explicitly because Stock has no cascade on address
        if (stock.getAddress() != null) {
            Address savedAddress = addressRepo.save(stock.getAddress());
            stock.setAddress(savedAddress);
        }
        return stockRepo.save(stock);
    }

    public Stock update(int id, Stock stock) {
        Stock s = findById(id);
        s.setName(stock.getName());
        s.setPhone(stock.getPhone());
        s.setEmail(stock.getEmail());

        // Update the existing address if it exists
        if (stock.getAddress() != null) {
            if (s.getAddress() != null) {
                // Update existing address
                Address existingAddress = s.getAddress();
                Address newAddress = stock.getAddress();
                existingAddress.setStreetNumber(newAddress.getStreetNumber());
                existingAddress.setStreetName(newAddress.getStreetName());
                existingAddress.setWard(newAddress.getWard());
                existingAddress.setDistrict(newAddress.getDistrict());
                existingAddress.setCity(newAddress.getCity());
                addressRepo.save(existingAddress);
            } else {
                // Create new address if stock doesn't have one
                Address savedAddress = addressRepo.save(stock.getAddress());
                s.setAddress(savedAddress);
            }
        }

        return stockRepo.save(s);
    }
    public int delete(int id) {
        Stock s = findById(id);
        
        try {
            // Set stock_id = NULL trong tất cả order_items liên quan đến stock này
            // Điều này tránh lỗi FK constraint khi xóa stock
            orderItemRepo.nullifyStockForOrderItems(id);
            
            // Xóa toàn bộ stock items
            List<StockItem> items = stockItemRepo.findByStock_Id(id);
            if (!items.isEmpty()) {
                stockItemRepo.deleteAll(items);
            }

            // Xóa stock
            stockRepo.delete(s);

            // Dọn địa chỉ nếu có
            if (s.getAddress() != null) {
                addressRepo.delete(s.getAddress());
            }
        } catch (DataIntegrityViolationException ex) {
            throw new AppException(ErrorCode.STOCK_DELETE_FAILED);
        }
        return id;
    }
    //Stock Item
    public List<StockProductResponse> getItemsOfStock(int id) {
        findById(id);
        List<StockItem> stockItems = stockItemRepo.findByStockIdWithProductInfo(id);

        // Group by product
        Map<Integer, List<StockItem>> groupedByProduct = stockItems.stream()
                .collect(Collectors.groupingBy(item -> item.getProductInfo().getProduct().getId()));

        return groupedByProduct.entrySet().stream()
                .map(entry -> {
                    Integer productId = entry.getKey();
                    List<StockItem> items = entry.getValue();
                    var product = items.get(0).getProductInfo().getProduct();
                    int totalQuantity = items.stream().mapToInt(StockItem::getQuantity).sum();

                    List<StockProductInfoResponse> variants = items.stream()
                            .map(item -> StockProductInfoResponse.builder()
                                    .id(item.getProductInfo().getId())
                                    .colorName(item.getProductInfo().getColorName())
                                    .colorHexCode(item.getProductInfo().getColorHexCode())
                                    .sizeName(item.getProductInfo().getSizeName())
                                    .image(item.getProductInfo().getImage())
                                    .quantity(item.getQuantity())
                                    .build())
                            .collect(Collectors.toList());

                    return StockProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .image(product.getImage())
                            .brand(product.getBrand())
                            .totalQuantity(totalQuantity)
                            .variants(variants)
                            .build();
                })
                .collect(Collectors.toList());
    }


    public StockItem setItemQuantity(int id, int productInfoId, int newQuantity) {
        //kiểm tra số lượng
        if (newQuantity < 0) throw new AppException(ErrorCode.INVALID_QUANTITY);

        Stock stock = findById(id);
        //tìm productInfo trong hệ thống
        ProductInfo productInfo = productInfoRepo.findById(productInfoId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_INFO_NOT_FOUND));
        //tìm sản phẩm có trong kho chưa nếu chưa thì tạo mới
        StockItem item = stockItemRepo.findByStock_IdAndProductInfo_Id(id, productInfoId)
                .orElseGet(() -> {
                    StockItem si = new StockItem();
                    si.setStock(stock);
                    si.setProductInfo(productInfo);
                    si.setQuantity(0);
                    return si;
                });

        item.setQuantity(newQuantity);
        return stockItemRepo.save(item);
    }


    public StockItem increaseItemQuantity(int id, int productInfoId, int amount) {
        if ( amount <= 0) throw new AppException(ErrorCode.INVALID_QUANTITY);

        StockItem item = stockItemRepo.findByStock_IdAndProductInfo_Id(id, productInfoId)
                .orElseGet(() -> setItemQuantity(id, productInfoId, 0));

        item.setQuantity(item.getQuantity() + amount);
        return stockItemRepo.save(item);
    }

    public StockItem decreaseItemQuantity(int id, int productInfoId, int amount) {
        if ( amount <= 0) throw new AppException(ErrorCode.INVALID_QUANTITY);

        StockItem item = stockItemRepo.findByStock_IdAndProductInfo_Id(id, productInfoId)
                .orElseThrow(() -> new AppException(ErrorCode.STOCK_ITEM_NOT_FOUND));

        int after = item.getQuantity() - amount;
        if (after < 0) throw new AppException(ErrorCode.NOT_ENOUGH_QUANTITY);

        item.setQuantity(after);
        return stockItemRepo.save(item);
    }
    // xóa sp khỏi kho
    public void deleteItem(int id, int productInfoId) {
        StockItem item = stockItemRepo.findByStock_IdAndProductInfo_Id(id, productInfoId)
                .orElseThrow(() -> new AppException(ErrorCode.STOCK_ITEM_NOT_FOUND));
        stockItemRepo.delete(item);
    }

    // Lấy tổng số lượng của tất cả variants của product từ tất cả kho
    public Map<Integer, Integer> getTotalQuantitiesForProduct(int productId) {
        List<StockItem> stockItems = stockItemRepo.findByProductInfo_Product_Id(productId);
        return stockItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProductInfo().getId(),
                        Collectors.summingInt(StockItem::getQuantity)
                ));
    }
}
