package com.alzzaipo.service;

import com.alzzaipo.domain.account.social.SocialAccountRepository;
import com.alzzaipo.domain.account.social.SocialCode;
import com.alzzaipo.dto.account.local.*;
import com.alzzaipo.util.EmailUtil;
import com.alzzaipo.util.JwtUtil;
import com.alzzaipo.domain.account.local.LocalAccount;
import com.alzzaipo.domain.account.local.LocalAccountRepository;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberType;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LocalAccountService {

    private final LocalAccountRepository localAccountRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final MemberService memberService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public LocalAccount register(LocalAccountRegisterRequestDto dto) {

        String accountId = dto.getAccountId();
        String accountPassword = dto.getAccountPassword();
        String email = dto.getEmail();
        String nickname = dto.getNickname();

        // 아이디 포맷 검사
        verifyAccountIdFormat(accountId);

        // 비밀번호 포맷 검사
        verifyPasswordFormat(accountPassword);

        // 이메일 포맷 검사
        EmailUtil.verifyEmailFormat(email);

        // 아이디 중복 체크
        localAccountRepository.findByAccountId(accountId)
                .ifPresent(localAccount -> {
                    throw new AppException(ErrorCode.ACCOUNT_ID_DUPLICATED, "중복된 아이디 입니다.");
                });

        // 이메일 중복 체크
        localAccountRepository.findByEmail(email)
                .ifPresent(localAccount -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, "중복된 이메일 입니다.");
                });

        // 이메일 인증 여부 확인
        if(emailService.getEmailVerificationStatus(email) == false) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "인증되지 않은 이메일 입니다.");
        }

        // 계정 생성
        Member member = memberService.createAndSave(nickname, MemberType.LOCAL);
        LocalAccount localAccount = LocalAccount.builder()
                .accountId(accountId)
                .accountPassword(encoder.encode(accountPassword))
                .email(email)
                .member(member)
                .build();

        return localAccountRepository.save(localAccount);
    }

    public String login(LocalAccountLoginRequestDto dto) {
        String accountId = dto.getAccountId();
        String accountPassword = dto.getAccountPassword();

        // 아이디 포맷 검사
        verifyAccountIdFormat(accountId);

        // 비밀번호 포맷 검사
        verifyPasswordFormat(accountPassword);

        // 계정 아이디 조회
        LocalAccount localAccount = localAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ACCOUNT_ID, "아이디 조회 실패"));

        // 계정 비밀번호 검사
        if(!encoder.matches(accountPassword, localAccount.getAccountPassword())) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT_PASSWORD, "비밀번호를 확인해 주세요.");
        }

        // 토큰 발행
        String token = jwtUtil.createToken(localAccount.getMember().getId());
        return token;
    }

    public void verifyAccountId(LocalAccountIdDto dto) {
        String accountId = dto.getAccountId();

        verifyAccountIdFormat(accountId);

        localAccountRepository.findByAccountId(accountId)
                .ifPresent(localAccount -> {
                    throw new AppException(ErrorCode.ACCOUNT_ID_DUPLICATED, "중복된 아이디 입니다.");
                });
    }

    public void verifyEmail(EmailDto dto) {
        String email = dto.getEmail();
        EmailUtil.verifyEmailFormat(email);

        localAccountRepository.findByEmail(email)
                .ifPresent(localAccount -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, "중복된 이메일 입니다.");
                });
    }

    public void verifyAccountIdFormat(String accountId) {
        String regex = "^[a-z0-9_-]{5,20}$";
        if(!Pattern.matches(regex, accountId)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "올바르지 않은 아이디 형식입니다.");
        }
    }

    public void verifyPasswordFormat(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?/]).{8,16}$";
        if(!Pattern.matches(regex, password)) {
            throw new AppException(ErrorCode.INVALID_PASSWORD_FORMAT, "올바르지 않은 비밀번호 형식입니다.");
        }
    }

    public LocalAccountProfileDto getLocalAccountProfileDto(Long memberId) {
        LocalAccount localAccount = localAccountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_MEMBER_ID, "해당 회원 정보를 찾을 수 없습니다."));

        // 아이디, 닉네임, 연동된 소셜 로그인 종류
        String accountId = localAccount.getAccountId();
        String nickname = localAccount.getMember().getNickname();
        List<SocialCode> socialLoginTypes = socialAccountRepository.findSocialLoginTypes(memberId);

        return new LocalAccountProfileDto(accountId, nickname, socialLoginTypes);
    }

    @Transactional
    public void updateProfile(Long memberId, LocalAccountProfileUpdateRequestDto dto) {
        LocalAccount localAccount = localAccountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_MEMBER_ID, "해당 회원 정보를 찾을 수 없습니다."));

        if (!localAccount.getEmail().equals(dto.getEmail())) {
            if (emailService.getEmailVerificationStatus(dto.getEmail()) == false) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "인증되지 않은 이메일 입니다.");
            }
            localAccount.changeEmail(dto.getEmail());
        }

        if (!localAccount.getMember().getNickname().equals(dto.getNickname())) {
            localAccount.getMember().changeNickname(dto.getNickname());
        }
    }

}
