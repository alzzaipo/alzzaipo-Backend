package com.alzzaipo.service;

import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberRepository;
import com.alzzaipo.enums.MemberType;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member createAndSave(String nickname, MemberType memberType) {
        Member member = Member.builder()
                .nickname(nickname)
                .memberType(memberType)
                .build();

        return memberRepository.save(member);
    }

    public Member findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.INVALID_MEMBER_ID, "회원 조회 실패");
                });

        return member;
    }

}