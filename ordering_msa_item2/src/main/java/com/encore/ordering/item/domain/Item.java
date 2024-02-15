package com.encore.ordering.item.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private int price;

    private int stockQuantity;

    @Setter
    private String imagePath;

    @Builder.Default
    private String delYn = "N";

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    public void deleteItem() {
        this.delYn = "Y";
    }

    public void updateItem(String name, String category, int price, int stockQuantity, String imagePath) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imagePath = imagePath;
    }

    // update할 값을 그대로 넘겨서 기존의 값을 갈아끼우는 것이 update의 의미에 더 부합한다.
    // 이곳에서 더하고 빼는 작업은 의미적으로 맞지 않는 것 같음.
    public void updateStockQuantity(int newQuantity) {
        this.stockQuantity = newQuantity;
    }

}
