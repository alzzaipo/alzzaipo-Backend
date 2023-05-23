package com.alzzaipo.service;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.domain.account.local.LocalAccount;
import com.alzzaipo.domain.account.social.SocialAccount;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberRepository;
import com.alzzaipo.dto.member.MemberProfileDto;
import com.alzzaipo.dto.member.MemberProfileUpdateRequestDto;
import com.alzzaipo.enums.LoginType;
import com.alzzaipo.enums.MemberType;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final SocialAccountService socialAccountService;
    private final EmailService emailService;

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
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

    public String getMemberNickname(Long memberId) {
        Member member = findById(memberId);
        return member.getNickname();
    }

    public MemberProfileDto getMemberProfileDto(MemberPrincipal memberInfo) {
        Member member = findById(memberInfo.getMemberId());
        MemberType memberType = member.getMemberType();


        if(memberType == MemberType.LOCAL) {
            String accountId = member.getLocalAccount().getAccountId();
            String email = member.getLocalAccount().getEmail();
            String nickname = member.getNickname();
            List<LoginType> linkedLoginTypes = socialAccountService.findLinkedSocialLoginTypes(member.getId());
            LoginType currentLoginType = memberInfo.getCurrentLoginType();

            MemberProfileDto dto = MemberProfileDto.builder()
                    .memberType(memberType)
                    .accountId(accountId)
                    .email(email)
                    .nickname(nickname)
                    .linkedLoginType(linkedLoginTypes)
                    .currentLoginType(currentLoginType)
                    .build();

            return dto;
        }
        else {
            SocialAccount socialAccount = socialAccountService.findByMemberIdAndLoginType(member.getId(), memberInfo.getCurrentLoginType())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_SOCIAL_ACCOUNT, "소셜 계정 조회 실패"));

            String email = socialAccount.getEmail();
            String nickname = member.getNickname();
            LoginType currentLoginType = memberInfo.getCurrentLoginType();

            MemberProfileDto dto = MemberProfileDto.builder()
                    .memberType(memberType)
                    .accountId("none")
                    .email(email)
                    .nickname(nickname)
                    .linkedLoginType(new ArrayList<>())
                    .currentLoginType(currentLoginType)
                    .build();

            return dto;
        }
    }

    @Transactional
    public void updateMemberProfile(MemberPrincipal memberInfo, MemberProfileUpdateRequestDto dto) {
        Member member = findById(memberInfo.getMemberId());

        if(member.getMemberType() == MemberType.LOCAL) {
            LocalAccount localAccount = member.getLocalAccount();
            String newEmail = dto.getEmail();

            if (emailService.getEmailVerificationStatus(newEmail) == false)
                throw new AppException(ErrorCode.UNAUTHORIZED, "인증되지 않은 이메일 입니다.");

            localAccount.changeEmail(newEmail);
        }

        member.changeNickname(dto.getNickname());
    }
}