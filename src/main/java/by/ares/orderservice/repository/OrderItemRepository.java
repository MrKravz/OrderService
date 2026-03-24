package by.ares.orderservice.repository;

import by.ares.orderservice.model.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

    @Query("SELECT o FROM OrderItem o WHERE o.order.id = :orderId AND o.item.id = :itemId")
    Optional<OrderItem> findByOrderIdAndItemId(Long orderId, Long itemId);

}
