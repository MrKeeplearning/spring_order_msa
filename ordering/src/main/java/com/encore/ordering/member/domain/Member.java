package com.encore.ordering.member.domain;

import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.order.domain.Ordering;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Ordering> orderings;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    public static Member toEntity(MemberCreateReqDto memberCreateReqDto) {
        Address address = new Address(
                memberCreateReqDto.getCity(),
                memberCreateReqDto.getStreet(),
                memberCreateReqDto.getZipcode());
        Member member = Member.builder()
                .name(memberCreateReqDto.getName())
                .email(memberCreateReqDto.getEmail())
                .password(memberCreateReqDto.getPassword())
                .address(address)
                .role(Role.USER)
                .build();
        return member;
    }
}
