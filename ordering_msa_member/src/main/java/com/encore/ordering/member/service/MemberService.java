package com.encore.ordering.member.service;

import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.LoginReqDto;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.dto.MemberResponseDto;
import com.encore.ordering.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(MemberCreateReqDto memberCreateReqDto) throws IllegalArgumentException {
        Optional<Member> member_check = memberRepository.findByEmail(memberCreateReqDto.getEmail());
        if (member_check.isPresent()) {
            throw new IllegalArgumentException("중복 이메일입니다.");
        }
        memberCreateReqDto.setPassword(passwordEncoder.encode(memberCreateReqDto.getPassword()));
        Member member = Member.toEntity(memberCreateReqDto);
        return memberRepository.save(member);
    }

    public MemberResponseDto findMyInfo(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return MemberResponseDto.toMemberResponseDto(member);
    }

    public List<MemberResponseDto> findAll() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(member -> MemberResponseDto.toMemberResponseDto(member)).collect(Collectors.toList());
    }

    public Member login(LoginReqDto loginReqDto) throws IllegalArgumentException {
        // 이메일 존재 여부
        Member member = memberRepository.findByEmail(loginReqDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email doesn't exist"));

        // password 일치 여부
        if (!passwordEncoder.matches(loginReqDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Password doesn't match");
        }
        return member;
    }
}
