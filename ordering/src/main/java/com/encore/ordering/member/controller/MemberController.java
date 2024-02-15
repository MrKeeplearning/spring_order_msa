package com.encore.ordering.member.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.LoginReqDto;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.dto.MemberResponseDto;
import com.encore.ordering.member.service.MemberService;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.service.OrderService;
import com.encore.ordering.securities.JwtTokenProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, OrderService orderService) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.orderService = orderService;
    }

    @PostMapping("/member/create")
    public ResponseEntity<CommonResponse> memberCreate(@Valid @RequestBody MemberCreateReqDto memberCreateReqDto) {
        Member member = memberService.create(memberCreateReqDto);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.CREATED, "Member created successfully",
                member.getId());
        return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
    }

    // 관리자의 경우에만 볼 수 있도록 처리해야 한다.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public List<MemberResponseDto> members() {
        return memberService.findAll();
    }

    @GetMapping("/member/myInfo")
    public MemberResponseDto findMyInfo() {
        return memberService.findMyInfo();
    }

    // get each user detailed orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{id}/orders")
    public List<OrderResDto> findMemberOrders(@PathVariable Long id) {
        return orderService.findByMember(id);
    }

    // get user's order info
    @GetMapping("/member/myorders")
    public List<OrderResDto> findMyOrders() {
        return orderService.findMyOrders();
    }

    @PostMapping("/doLogin")
    public ResponseEntity<CommonResponse> memberLogin(@Valid @RequestBody LoginReqDto loginReqDto) {
        Member member = memberService.login(loginReqDto);

        // 토큰 생성 로직
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());

        Map<String, Object> member_info = new HashMap<>();
        member_info.put("id", member.getId());
        member_info.put("token", jwtToken);
        return new ResponseEntity<>(
                new CommonResponse(
                        HttpStatus.CREATED,
                        "Member created successfully logined",
                        member_info),
                HttpStatus.OK);
    }
}
