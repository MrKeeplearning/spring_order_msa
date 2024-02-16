package com.encore.ordering.order.dto;

import lombok.Data;

@Data
public class ItemQuantityUpdateDto {
    private Long id;
    private int stockQuantity;
}
