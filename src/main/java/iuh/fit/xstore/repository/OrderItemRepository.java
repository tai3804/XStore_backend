package iuh.fit.xstore.repository;

import iuh.fit.xstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    // Kiểm tra xem stock có được sử dụng trong order_items không
    long countByStockId(int stockId);
    
    List<OrderItem> findByStockId(int stockId);
    
    // Set stock_id = NULL cho tất cả order_items của stock
    @Modifying
    @Transactional
    @Query("UPDATE OrderItem oi SET oi.stock = NULL WHERE oi.stock.id = :stockId")
    void nullifyStockForOrderItems(@Param("stockId") int stockId);
}

