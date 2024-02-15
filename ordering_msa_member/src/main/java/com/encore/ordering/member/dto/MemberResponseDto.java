package com.encore.ordering.member.dto;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String street;
    private String zipcode;
    private int orderCount;

    // builder 내부에서 분기처리를 할 때 아래와 같이 작성할 수 있다.
    public static MemberResponseDto toMemberResponseDto(Member member) {
        MemberResponseDtoBuilder builder = MemberResponseDto.builder();
        builder.id(member.getId());
        builder.name(member.getName());
        builder.email(member.getEmail());
//        builder.orderCount(member.getOrderings().size());
        Address address = member.getAddress();
        if (address != null) {
            builder.city(address.getCity());
            builder.street(address.getStreet());
            builder.zipcode(address.getZipcode());
        }
        return builder.build();
    }
}
