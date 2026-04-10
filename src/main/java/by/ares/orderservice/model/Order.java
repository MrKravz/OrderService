package by.ares.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static by.ares.orderservice.util.OrderServiceConstants.DEFAULT_TOTAL_PRICE_VALUE;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Accessors(chain = true)
@SQLRestriction("deleted = 'false'")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE orders SET deleted = true WHERE id=?")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        calculateTotalPrice();
    }

    public void removeOrderItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        totalPrice = DEFAULT_TOTAL_PRICE_VALUE;
        if (getItems().isEmpty()) return;
        items.forEach(orderItem -> totalPrice += orderItem.getItem().getPrice() * orderItem.getQuantity());
    }

}
