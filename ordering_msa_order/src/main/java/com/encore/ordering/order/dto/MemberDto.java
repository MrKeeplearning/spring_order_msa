package com.encore.ordering.order.dto;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String street;
    private String zipcode;
    private int orderCount;
}
