package com.alzzaipo.service;

import com.alzzaipo.domain.account.local.LocalAccount;
import com.alzzaipo.domain.account.local.LocalAccountRepository;
import com.alzzaipo.domain.account.social.SocialAccountRepository;
import com.alzzaipo.domain.account.social.SocialCode;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberType;
import com.alzzaipo.dto.account.local.LocalAccountLoginRequestDto;
import com.alzzaipo.dto.account.local.LocalAccountProfileResponseDto;
import com.alzzaipo.dto.account.local.LocalAccountProfileUpdateRequestDto;
import com.alzzaipo.dto.account.local.LocalAccountRegisterRequestDto;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import com.alzzaipo.util.DataFormatUtil;
import com.alzzaipo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 멤버 인덱스로 로컬 계정 조회, 실패 시 예외 발생 -> BAD_REQUEST
    private LocalAccount findLocalAccountByMemberId(Long memberId) {
        LocalAccount localAccount = localAccountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_MEMBER_ID, "회원 조회 실패"));
        return localAccount;
    }


    // 아이디 중복 여부 확인, 중복 시 예외 발생 -> CONFLICT 응답
    public void verifyAccountIdUsability(String accountId) {
        DataFormatUtil.validateAccountIdFormat(accountId);

        localAccountRepository.findByAccountId(accountId)
                .ifPresent(localAccount -> {
                    throw new AppException(ErrorCode.ACCOUNT_ID_DUPLICATED, "중복된 아이디 입니다.");
                });
    }

    // 이메일 중복 여부 확인, 중복 시 예외 발생 -> CONFLICT 응답
    public void verifyEmailUsability(String email) {
        DataFormatUtil.validateEmailFormat(email);

        localAccountRepository.findByEmail(email)
                .ifPresent(localAccount -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, "중복된 이메일 입니다.");
                });
    }

    @Transactional
    public LocalAccount register(LocalAccountRegisterRequestDto dto) {
        String accountId = dto.getAccountId();
        String accountPassword = dto.getAccountPassword();
        String email = dto.getEmail();
        String nickname = dto.getNickname();

        // 아이디 포맷 검사
        DataFormatUtil.validateAccountIdFormat(accountId);

        // 비밀번호 포맷 검사
        DataFormatUtil.validateAccountPasswordFormat(accountPassword);

        // 이메일 포맷 검사
        DataFormatUtil.validateEmailFormat(email);

        // 아이디 중복 체크
        verifyAccountIdUsability(accountId);

        // 이메일 중복 체크
        verifyEmailUsability(email);

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
        String userInputAccountId = dto.getAccountId();
        String userInputAccountPassword = dto.getAccountPassword();

        // 아이디 포맷 검사
        DataFormatUtil.validateAccountIdFormat(userInputAccountId);

        // 비밀번호 포맷 검사
        DataFormatUtil.validateAccountPasswordFormat(userInputAccountPassword);

        // 아이디로 로컬 계정 찾기
        LocalAccount localAccount = localAccountRepository.findByAccountId(userInputAccountId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ACCOUNT_ID, "아이디 조회 실패"));

        // 계정 비밀번호 검사
        String storedAccountPassword = localAccount.getAccountPassword();
        if(!encoder.matches(userInputAccountPassword, storedAccountPassword)) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT_PASSWORD, "비밀번호를 확인해 주세요.");
        }

        // 토큰 발행
        String token = jwtUtil.createToken(localAccount.getMember().getId());
        return token;
    }

    // 계정 프로필 정보 조회
    public LocalAccountProfileResponseDto getLocalAccountProfileDto(Long memberId) {
        LocalAccount localAccount = findLocalAccountByMemberId(memberId);

        // 아이디, 닉네임, 이메일, 연동된 소셜 로그인 종류
        String accountId = localAccount.getAccountId();
        String nickname = localAccount.getMember().getNickname();
        String email = localAccount.getEmail();
        List<SocialCode> socialLoginTypes = socialAccountRepository.findSocialLoginTypes(memberId);

        return new LocalAccountProfileResponseDto(accountId, nickname, email, socialLoginTypes);
    }

    // 계정 프로필 정보 수정
    @Transactional
    public void updateProfile(Long memberId, LocalAccountProfileUpdateRequestDto dto) {
        LocalAccount localAccount = findLocalAccountByMemberId(memberId);

        String storedEmail = localAccount.getEmail();
        String storedNickname = localAccount.getMember().getNickname();
        String userInputEmail = dto.getEmail();
        String userInputNickname = dto.getNickname();

        if (!storedEmail.equals(userInputEmail)) {
            if (emailService.getEmailVerificationStatus(userInputEmail) == false) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "인증되지 않은 이메일 입니다.");
            }
            localAccount.changeEmail(userInputEmail);
        }

        if (!storedNickname.equals(userInputNickname)) {
            localAccount.getMember().changeNickname(userInputNickname);
        }
    }

}
