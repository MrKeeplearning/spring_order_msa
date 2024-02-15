package com.encore.ordering.order.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/create")
    public ResponseEntity<CommonResponse> orderCreate(@RequestBody List<OrderReqDto> orderReqDtos) {
        Ordering ordering = orderService.create(orderReqDtos);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.CREATED, "Order created successfully", ordering.getId());
        return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
    }

    // 넘겨온 사용자의 이메일 정보가 토큰 안에 든 이메일 정보와 일치할 경우에...
//    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principle.username")
    @DeleteMapping("/order/{id}/cancel")
    public ResponseEntity<CommonResponse> orderCancel(@PathVariable Long id) {
        Ordering ordering = orderService.cancel(id);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Order cancelled successfully", ordering.getId());
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public List<OrderResDto> orderList() {
        return orderService.findAll();
    }

}
