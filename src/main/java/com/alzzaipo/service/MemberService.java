package com.alzzaipo.service;

import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberRepository;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import com.alzzaipo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;
    private Long expiredMillis = 1000 * 60 * 60l;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findMemberById(Long memberId) { return memberRepository.findById(memberId); }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member join(String accountId, String accountPassword, String email, String nickname) {

        // 계정 아이디 중복체크
        memberRepository.findByAccountId(accountId)
                        .ifPresent(member -> {
                            throw new AppException(ErrorCode.ACCOUNT_ID_DUPLICATED, accountId + " 은 이미 있습니다.");
                        });

        // 이메일 중복 체크
        memberRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED, email + " 은 이미 있습니다.");
                });

        // 저장
        Member member = Member.builder()
                .accountId(accountId)
                .accountPassword(encoder.encode(accountPassword))
                .email(email)
                .nickname(nickname)
                .build();

        return memberRepository.save(member);
    }

    public String login(String accountId, String accountPassword) {

        // 계정 아이디 확인
        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_ID_NOT_FOUND, "존재하지 않는 아이디 입니다."));

        // 계정 비밀번호 확인
        if(!encoder.matches(accountPassword, member.getAccountPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }

        // 토큰 발행
        String token = JwtUtil.createToken(accountId, jwtSecretKey, expiredMillis);
        return token;
    }
}
