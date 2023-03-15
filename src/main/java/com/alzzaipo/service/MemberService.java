package com.alzzaipo.service;

import com.alzzaipo.domain.user.Member;
import com.alzzaipo.domain.user.MemberRepository;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findMemberById(Long memberId) { return memberRepository.findById(memberId); }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void join(String uid, String password, String email, String nickname) {

        // uid 중복체크
        memberRepository.findByUid(uid)
                        .ifPresent(user -> {
                            throw new AppException(ErrorCode.DUPLICATE_UID, uid + "는 이미 있습니다.");
                        });

        // email 중복 체크
        memberRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATE_EMAIL, email + "은 이미 있습니다.");
                });

        // 저장
        Member.builder()
                .uid(uid)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
