package com.encore.ordering.order.service;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.order.domain.OrderItem;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.ItemDto;
import com.encore.ordering.order.dto.ItemQuantityUpdateDto;
import com.encore.ordering.order.dto.MemberDto;
import com.encore.ordering.order.dto.OrderReqDto;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class OrderService {

    private final String MEMBER_API = "http://member-service/";
    private final String ITEM_API = "http://item-service/";

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public Ordering create(List<OrderReqDto> orderReqDtos, String email) {
        String url = MEMBER_API + "member/findByEmail?email=" + email;
        MemberDto member = restTemplate.getForObject(url, MemberDto.class);
        Ordering ordering = Ordering.builder().memberId(member.getId()).build();
        List<ItemQuantityUpdateDto> itemQuantityUpdateDtos = new ArrayList<>();

        // Ordering 객체가 생성될 때 OrderingItem 객체도 함께 생성 : cascading
        for (OrderReqDto dto : orderReqDtos) {
            OrderItem orderItem = OrderItem.builder()
                    .itemId(dto.getItemId())
                    .quantity(dto.getCount())
                    .ordering(ordering)
                    .build();
            ordering.getOrderItems().add(orderItem);

            String itemUrl = ITEM_API + "item/" + dto.getItemId();
            ResponseEntity<ItemDto> itemResponseEntity = restTemplate.getForEntity(itemUrl, ItemDto.class);

            if(itemResponseEntity.getBody().getStockQuantity() - dto.getCount() < 0) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }

            int newQuantity = itemResponseEntity.getBody().getStockQuantity() - dto.getCount();
            ItemQuantityUpdateDto itemQuantityUpdateDto = new ItemQuantityUpdateDto();
            itemQuantityUpdateDto.setId(dto.getItemId());
            itemQuantityUpdateDto.setStockQuantity(newQuantity);
            itemQuantityUpdateDtos.add(itemQuantityUpdateDto);

        }
        Ordering ordering1 = orderRepository.save(ordering);
        // orderRepository.save를 먼저 함으로서 위 코드에서 에러 발생 시 item 서비스 호출하지 않기 때문에 트랜잭션 문제가 발생하지 않음.
        String itemPatchUrl = ITEM_API + "item/updateQuantity";
        HttpEntity<List<ItemQuantityUpdateDto>> entity = new HttpEntity<>(itemQuantityUpdateDtos);
        ResponseEntity<CommonResponse> response = restTemplate
                .exchange(itemPatchUrl, HttpMethod.POST, entity, CommonResponse.class);

        // 만약 위 업데이트 이후에 추가적인 로직이 존재하면 트랜잭션 오류는 여전히 발생가능하다.
        // 해결책으로 에러 발생할 가능성이 있는 코드 전체를 try catch로 예외처리 이후에 catch에서 updateRollbackQuantity를 추가로 만들어서 호출
        return ordering1;
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
