package com.alzzaipo.service;

import com.alzzaipo.domain.account.social.SocialAccount;
import com.alzzaipo.domain.account.social.SocialAccountRepository;
import com.alzzaipo.enums.SocialCode;
import com.alzzaipo.dto.account.social.SocialAccountInfo;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.enums.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SocialAccountService {

    private final SocialAccountRepository socialAccountRepository;
    private final MemberService memberService;

    // 이메일과 소셜 코드(NAVER, KAKAO, GOOGLE)로 가입 여부를 조회
    public Optional<SocialAccount> findByEmailAndSocialCode(String email, SocialCode socialCode) {
        return socialAccountRepository.findByEmailAndSocialCode(email, socialCode);
    }

    @Transactional
    public void registerIfNeeded(SocialAccountInfo accountInfo, SocialCode socialCode) {
        Optional<SocialAccount> registeredAccount = findByEmailAndSocialCode(accountInfo.getEmail(), socialCode);

        // 가입되어 있지 않다면 가입 처리
        if(registeredAccount.isEmpty()) {
            Member member = memberService.createAndSave(accountInfo.getNickname(), MemberType.SOCIAL);

            SocialAccount socialAccount = SocialAccount.builder()
                    .email(accountInfo.getEmail())
                    .socialCode(socialCode)
                    .member(member)
                    .build();

            socialAccountRepository.save(socialAccount);
        }
    }

}
