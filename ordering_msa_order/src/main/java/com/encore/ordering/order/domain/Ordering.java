package com.encore.ordering.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@NoArgsConstructor
public class Ordering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @OneToMany(mappedBy = "ordering", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @Builder
    public Ordering(Long memberId) {
        this.memberId = memberId;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELED;
    }
}
