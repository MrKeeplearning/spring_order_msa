package com.encore.ordering.order.dto;

import lombok.Data;

@Data
public class OrderReqDto {

    private Long itemId;
    private int count;

}
/*
"orderReqItemDtos": [
{"itemId":1, "count":10},
{"itemId":2, "count":20}
]
 */

// 예시데이터
/*
{
    "itemIds":[1, 2], "counts":[10, 20]
}
 */
