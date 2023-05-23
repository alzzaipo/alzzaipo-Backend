package com.alzzaipo.service;

import com.alzzaipo.domain.account.local.LocalAccount;
import com.alzzaipo.domain.account.local.LocalAccountRepository;
import com.alzzaipo.domain.account.social.SocialAccountRepository;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.enums.LoginType;
import com.alzzaipo.enums.MemberType;
import com.alzzaipo.dto.account.local.*;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.enums.ErrorCode;
import com.alzzaipo.util.DataFormatUtil;
import com.alzzaipo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LocalAccountService {

    private final LocalAccountRepository localAccountRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final MemberService memberService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 멤버 인덱스로 로컬 계정 조회, 실패 시 예외 발생 -> BAD_REQUEST
    public LocalAccount findLocalAccountByMemberId(Long memberId) {
        LocalAccount localAccount = localAccountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_MEMBER_ID, "회원 조회 실패"));
        return localAccount;
    }

    // 사용자 입력 비밀번호가 계정 비밀번호와 일치하는지 검사
    private boolean checkLocalAccountPassword(String userInputAccountPassword, String storedAccountPassword) {
        return passwordEncoder.matches(userInputAccountPassword, storedAccountPassword);
    }

    // 사용자 입력 비밀번호가 일치하지 않으면 예외 발생 -> Unauthorized 응답
    private void verifyLocalAccountPassword(String userInputAccountPassword, String storedAccountPassword) {
        if(!checkLocalAccountPassword(userInputAccountPassword, storedAccountPassword)) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT_PASSWORD, "비밀번호를 확인해 주세요.");
        }
    }

    // 아이디 중복 여부 조회, 중복 시 예외 발생 -> CONFLICT 응답
    public void verifyAccountIdUsability(String accountId) {
        DataFormatUtil.validateAccountIdFormat(accountId);

        localAccountRepository.findByAccountId(accountId)
                .ifPresent(localAccount -> {
                    throw new AppException(ErrorCode.ACCOUNT_ID_DUPLICATED, "중복된 아이디 입니다.");
                });
    }

    // 이메일 중복 여부 조회, 중복 시 예외 발생 -> CONFLICT 응답
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
                .accountPassword(passwordEncoder.encode(accountPassword))
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
        verifyLocalAccountPassword(userInputAccountPassword, storedAccountPassword);

        // 토큰 발행
        String token = jwtUtil.createToken(localAccount.getMember().getId(), LoginType.LOCAL);
        return token;
    }

    @Transactional
    public void changePassword(Long memberId, LocalAccountPasswordChangeRequestDto dto) {
        // 사용자 입력 기존 비밀번호와 새로운 비밀번호
        String userInputCurrentAccountPassword = dto.getCurrentAccountPassword();
        String userInputNewAccountPassword = dto.getNewAccountPassword();

        // 사용자 입력 기존 비밀번호 포맷 검사
        DataFormatUtil.validateAccountPasswordFormat(userInputCurrentAccountPassword);

        // 새로운 비밀번호 포맷 검사
        DataFormatUtil.validateAccountPasswordFormat(userInputNewAccountPassword);

        // 계정 엔티티 조회
        LocalAccount localAccount = findLocalAccountByMemberId(memberId);

        // 사용자 입력 기존 비밀번호 검증
        String storedAccountPassword = localAccount.getAccountPassword();
        verifyLocalAccountPassword(userInputCurrentAccountPassword, storedAccountPassword);

        // 새로운 비밀번호가 기존의 비밀번호와 다르게 입력되었는지 검사
        if(userInputCurrentAccountPassword.equals(userInputNewAccountPassword)) {
            throw new AppException(ErrorCode.INVALID_NEW_PASSWORD, "기존의 비밀번호와 같은 비밀번호 입니다.");
        }

        // 새로운 비밀번호로 변경
        String newEncodedAccountPassword = passwordEncoder.encode(userInputNewAccountPassword);
        localAccount.changeAccountPassword(newEncodedAccountPassword);
    }
}
