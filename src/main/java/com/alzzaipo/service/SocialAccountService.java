package com.alzzaipo.service;

import com.alzzaipo.domain.account.social.SocialAccount;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberRepository;
import com.alzzaipo.dto.account.social.SocialAccountInfo;
import com.alzzaipo.enums.ErrorCode;
import com.alzzaipo.enums.LoginType;
import com.alzzaipo.enums.MemberType;
import com.alzzaipo.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SocialAccountService {

    private final com.alzzaipo.domain.account.social.SocialAccountRepository socialAccountRepository;
    private final MemberRepository memberRepository;

    // 이메일과 소셜 코드(NAVER, KAKAO, GOOGLE)로 가입 여부를 조회
    public Optional<SocialAccount> findByEmailAndLoginType(String email, LoginType loginType) {
        return socialAccountRepository.findByEmailAndLoginType(email, loginType);
    }

    public Optional<SocialAccount> findByMemberIdAndLoginType(Long memberId, LoginType loginType) {
        return socialAccountRepository.findByMemberIdAndLoginType(memberId, loginType);
    }

    @Transactional
    public void registerIfNeeded(SocialAccountInfo accountInfo, LoginType loginType) {
        Optional<SocialAccount> registeredAccount = findByEmailAndLoginType(accountInfo.getEmail(), loginType);

        // 가입되어 있지 않다면 가입 처리
        if(registeredAccount.isEmpty()) {
            Member member = Member.builder()
                    .nickname(accountInfo.getNickname())
                    .memberType(MemberType.SOCIAL)
                    .build();

            memberRepository.save(member);


            SocialAccount socialAccount = SocialAccount.builder()
                    .email(accountInfo.getEmail())
                    .loginType(loginType)
                    .member(member)
                    .build();

            socialAccountRepository.save(socialAccount);
        }
    }

    public List<LoginType> findLinkedSocialLoginTypes(Long memberId) {
        List<LoginType> linkedSocialLoginTypes = socialAccountRepository.findLinkedSocialLoginTypes(memberId);
        return linkedSocialLoginTypes;
    }

    @Transactional
    public void connectNewAccount(Long memberId, SocialAccountInfo socialAccountInfo, LoginType loginType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_MEMBER_ID, "회원 조회 실패"));

        SocialAccount connectedSocialAccount = SocialAccount.builder()
                .email(socialAccountInfo.getEmail())
                .loginType(loginType)
                .member(member)
                .build();

        socialAccountRepository.save(connectedSocialAccount);
    }
}
