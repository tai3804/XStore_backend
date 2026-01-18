package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.request.ProductFilterRequest;
import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductInfo;
import iuh.fit.xstore.model.ProductType;
import iuh.fit.xstore.model.StockItem;
import iuh.fit.xstore.repository.ProductRepository;
import iuh.fit.xstore.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StockItemRepository stockItemRepository;
    
    public List<Product> findAll() {
        return productRepository.findAllWithComments();
    }
    
    public Product findById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }
    
    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }
    
    public List<Product> findByType(ProductType type) {
        return productRepository.findByType(type);
    }
    
    public List<Product> findByTypeId(int typeId) {
        return productRepository.findByTypeId(typeId);
    }
    
    public List<Product> findByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }
    
    @Transactional
    public Product createProduct(Product product) {
        System.out.println("Creating product: " + product.getName());
        System.out.println("   ProductInfos: " + (product.getProductInfos() != null ? product.getProductInfos().size() : 0));
        
        // Lưu product (cascade sẽ tự động lưu productInfos)
        Product savedProduct = productRepository.save(product);
        System.out.println("Product saved with ID: " + savedProduct.getId());
        
        return savedProduct;
    }
    
    @Transactional
    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setImage(product.getImage());
        existingProduct.setType(product.getType());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setFabric(product.getFabric());
        existingProduct.setPriceInStock(product.getPriceInStock());
        existingProduct.setPrice(product.getPrice());
        
        // ProductInfo sẽ được quản lý qua ProductInfoService riêng
        
        return productRepository.save(existingProduct);
    }
    
    public int deleteProduct(int id) {
        findById(id); // Check if product exists
        productRepository.deleteById(id);
        return id;
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAllWithComments();
        }
        return productRepository.searchProducts(keyword.trim().toLowerCase());
    }

    public List<Object> getProductStocks(int productId) {
        // Verify product exists
        Product product = findById(productId);
        
        // Get all product infos for this product
        List<ProductInfo> productInfos = product.getProductInfos();
        
        // For each product info, get stock items
        List<Object> result = new ArrayList<>();
        for (ProductInfo info : productInfos) {
            List<StockItem> stockItems = stockItemRepository.findByProductInfo_Id(info.getId());
            for (StockItem item : stockItems) {
                java.util.Map<String, Object> stockInfo = new java.util.HashMap<>();
                stockInfo.put("stockId", item.getStock().getId());
                stockInfo.put("stockName", item.getStock().getName());
                stockInfo.put("productInfoId", info.getId());
                stockInfo.put("colorName", info.getColorName());
                stockInfo.put("sizeName", info.getSizeName());
                result.add(stockInfo);
            }
        }
        
        return result;
    }



    /**
     * Filter products with advanced criteria
     * @param filterRequest Filter criteria
     * @return Filtered and sorted list of products
     */
    public List<Product> filterProducts(ProductFilterRequest filterRequest) {
        List<Product> products = productRepository.findAllWithComments();

        // Filter by product type ID
        if (filterRequest.getProductTypeId() != null) {
            products = products.stream()
                    .filter(p -> p.getType() != null && p.getType().getId() == filterRequest.getProductTypeId())
                    .collect(Collectors.toList());
        }

        // Filter by product type name
        if (filterRequest.getProductTypeName() != null && !filterRequest.getProductTypeName().trim().isEmpty()) {
            String typeName = filterRequest.getProductTypeName().trim();
            products = products.stream()
                    .filter(p -> p.getType() != null && p.getType().getName().equalsIgnoreCase(typeName))
                    .collect(Collectors.toList());
        }

        // Filter by minimum price
        if (filterRequest.getMinPrice() != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() >= filterRequest.getMinPrice())
                    .collect(Collectors.toList());
        }

        // Filter by maximum price
        if (filterRequest.getMaxPrice() != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() <= filterRequest.getMaxPrice())
                    .collect(Collectors.toList());
        }

        // Sort products
        if (filterRequest.getSortBy() != null && !filterRequest.getSortBy().isEmpty()) {
            switch (filterRequest.getSortBy().toLowerCase()) {
                case "price-asc":
                    products.sort(Comparator.comparingDouble(Product::getPrice));
                    break;
                case "price-desc":
                    products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
                    break;
                case "name-asc":
                    products.sort(Comparator.comparing(Product::getName));
                    break;
                case "name-desc":
                    products.sort(Comparator.comparing(Product::getName).reversed());
                    break;
                case "newest":
                    products.sort(Comparator.comparingInt(Product::getId).reversed());
                    break;
                default:
                    // Keep default order
                    break;
            }
        }

        return products;
    }
}