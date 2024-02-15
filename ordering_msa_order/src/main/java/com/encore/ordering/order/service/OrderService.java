package com.encore.ordering.order.service;

import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Ordering create(List<OrderReqDto> orderReqDtos) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("not found email"));
//        Ordering ordering = Ordering.builder().member(member).build();
//
//        // Ordering 객체가 생성될 때 OrderingItem 객체도 함께 생성 : cascading
//        for (OrderReqDto dto : orderReqDtos) {
//            Item item = itemRepository.findById(dto.getItemId())
//                    .orElseThrow(() -> new EntityNotFoundException("not found item"));
//            OrderItem orderItem = OrderItem.builder()
//                    .item(item)
//                    .quantity(dto.getCount())
//                    .ordering(ordering)
//                    .build();
//            ordering.getOrderItems().add(orderItem);
//            if(item.getStockQuantity() - dto.getCount() < 0) {
//                throw new IllegalArgumentException("재고가 부족합니다.");
//            }
//            orderItem.getItem().updateStockQuantity(item.getStockQuantity() - dto.getCount());
//        }
//        return orderRepository.save(ordering);
        return null;
    }

    // 1. User who ordered 'order' and ADMIN user can only cancel order.
    // 2. Find logined user.
    // 3. Find Ordering and set status as CANCELED. Then, revoke item stockQuantity.
    public Ordering cancel(Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//
//        Ordering ordering = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found order"));
//        if (!ordering.getMember().getEmail().equals(email) && !authentication.getAuthorities().contains((new SimpleGrantedAuthority("ROLE_ADMIN")))) {
//            throw new AccessDeniedException("권한이 없습니다.");
//        }
//        if (ordering.getOrderStatus() == OrderStatus.CANCELED) {
//            throw new IllegalArgumentException("이미 취소된 주문입니다.");
//        }
//        ordering.cancelOrder();
//        for(OrderItem orderItem : ordering.getOrderItems()) {
//            orderItem.getItem().updateStockQuantity(orderItem.getItem().getStockQuantity() + orderItem.getQuantity());
//        }
//        return ordering;
            return null;
    }

    public List<OrderResDto> findAll() {
        List<Ordering> orderings = orderRepository.findAll();
        return orderings.stream().map(o -> OrderResDto.toDto(o)).collect(Collectors.toList());
    }

    // find each user's ordering info
    public List<OrderResDto> findByEmail(Long memberId) {

        List<Ordering> orderings = orderRepository.findByMemberId(memberId);
        return orderings.stream().map(o -> OrderResDto.toDto(o)).collect(Collectors.toList());
    }

//    public List<OrderResDto> findByMember(Long id) {
//        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found email"));
////        List<Ordering> orderings = member.getOrderings();
//        List<Ordering> orderings = orderRepository.findByMemberId(id);
//        return orderings.stream().map(o -> OrderResDto.toDto(o)).collect(Collectors.toList());
//    }

    public List<OrderResDto> findMyOrders(Long memberId) {
        List<Ordering> orderings = orderRepository.findByMemberId(memberId);
        return orderings.stream().map(o -> OrderResDto.toDto(o)).collect(Collectors.toList());
    }

}
