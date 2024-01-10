package com.alzzaipo.member.adapter.in.web;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.member.adapter.in.web.dto.ChangeLocalAccountPasswordWebRequest;
import com.alzzaipo.member.adapter.in.web.dto.CheckLocalAccountEmailVerificationCodeRequest;
import com.alzzaipo.member.adapter.in.web.dto.EmailDto;
import com.alzzaipo.member.adapter.in.web.dto.LocalAccountPasswordDto;
import com.alzzaipo.member.adapter.in.web.dto.RegisterLocalAccountWebRequest;
import com.alzzaipo.member.adapter.in.web.dto.UpdateMemberProfileWebRequest;
import com.alzzaipo.member.application.port.in.account.local.ChangeLocalAccountPasswordUseCase;
import com.alzzaipo.member.application.port.in.account.local.CheckEmailVerificationCodeQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailableQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountIdAvailableQuery;
import com.alzzaipo.member.application.port.in.account.local.RegisterLocalAccountUseCase;
import com.alzzaipo.member.application.port.in.account.local.SendSignUpEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.SendUpdateLocalAccountEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.VerifyLocalAccountPasswordQuery;
import com.alzzaipo.member.application.port.in.dto.CheckLocalAccountEmailVerificationCodeCommand;
import com.alzzaipo.member.application.port.in.dto.MemberProfile;
import com.alzzaipo.member.application.port.in.member.FindMemberNicknameQuery;
import com.alzzaipo.member.application.port.in.member.FindMemberProfileQuery;
import com.alzzaipo.member.application.port.in.member.UpdateMemberProfileUseCase;
import com.alzzaipo.member.application.port.in.member.WithdrawMemberUseCase;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final RegisterLocalAccountUseCase registerLocalAccountUseCase;
    private final CheckLocalAccountIdAvailableQuery checkLocalAccountIdAvailableQuery;
    private final CheckLocalAccountEmailAvailableQuery checkLocalAccountEmailAvailableQuery;
    private final VerifyLocalAccountPasswordQuery verifyLocalAccountPasswordQuery;
    private final ChangeLocalAccountPasswordUseCase changeLocalAccountPasswordUseCase;
    private final FindMemberNicknameQuery findMemberNicknameQuery;
    private final FindMemberProfileQuery findMemberProfileQuery;
    private final UpdateMemberProfileUseCase updateMemberProfileUseCase;
    private final WithdrawMemberUseCase withdrawMemberUseCase;
    private final SendSignUpEmailVerificationCodeUseCase sendSignUpEmailVerificationCodeUseCase;
    private final CheckEmailVerificationCodeQuery checkEmailVerificationCodeQuery;
    private final SendUpdateLocalAccountEmailVerificationCodeUseCase sendUpdateLocalAccountEmailVerificationCodeUseCase;

    @GetMapping("/register/verify-account-id")
    public ResponseEntity<String> checkLocalAccountIdAvailability(@RequestParam("accountId") String accountId) {
        if (!checkLocalAccountIdAvailableQuery.checkAccountIdAvailable(new LocalAccountId(accountId))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 아이디 입니다.");
        }
        return ResponseEntity.ok().body("사용 가능한 아이디 입니다.");
    }

    @GetMapping("/register/verify-email")
    public ResponseEntity<String> checkLocalAccountEmailAvailability(@RequestParam("email") String email) {
        if (!checkLocalAccountEmailAvailableQuery.checkEmailAvailable(new Email(email))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 이메일 입니다.");
        }
        return ResponseEntity.ok().body("사용 가능한 이메일 입니다.");
    }

    @PostMapping("/register/send-verification-code")
    public ResponseEntity<String> sendSignUpVerificationCode(@Valid @RequestBody EmailDto emailDto) {
        sendSignUpEmailVerificationCodeUseCase.sendSignUpEmailVerificationCode(new Email(emailDto.getEmail()));
        return ResponseEntity.ok().body("전송 완료");
    }

    @PostMapping("/register/validate-verification-code")
    public ResponseEntity<String> validateVerificationCode(
        @Valid @RequestBody CheckLocalAccountEmailVerificationCodeRequest request
    ) {
        CheckLocalAccountEmailVerificationCodeCommand command = request.toCommand(EmailVerificationPurpose.SIGN_UP);

        if (checkEmailVerificationCodeQuery.checkEmailVerificationCode(command)) {
            return ResponseEntity.ok().body("인증 성공");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterLocalAccountWebRequest dto) {
        registerLocalAccountUseCase.registerLocalAccount(dto.toCommand());
        return ResponseEntity.ok().body("가입 완료");
    }

    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(
        @AuthenticationPrincipal MemberPrincipal principal,
        @Valid @RequestBody LocalAccountPasswordDto dto
    ) {
        Id memberId = principal.getMemberId();
        LocalAccountPassword localAccountPassword = new LocalAccountPassword(dto.getAccountPassword());

        if (verifyLocalAccountPasswordQuery.verifyLocalAccountPassword(memberId, localAccountPassword)) {
            return ResponseEntity.ok().body("비밀번호 검증 성공");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 검증 실패");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
        @AuthenticationPrincipal MemberPrincipal principal,
        @Valid @RequestBody ChangeLocalAccountPasswordWebRequest request
    ) {
        if (changeLocalAccountPasswordUseCase.changePassword(request.toCommand(principal.getMemberId()))) {
            return ResponseEntity.ok().body("비밀번호 변경 완료");
        }
        return ResponseEntity.internalServerError().body("비밀번호 변경 실패");
    }

    @GetMapping("/nickname")
    public ResponseEntity<String> findMemberNickname(@AuthenticationPrincipal MemberPrincipal principal) {
        String nickname = findMemberNicknameQuery.findMemberNickname(principal.getMemberId());
        return ResponseEntity.ok().body(nickname);
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberProfile> findMemberProfile(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberProfile memberProfile = findMemberProfileQuery.findMemberProfile(
            principal.getMemberId(),
            principal.getCurrentLoginType());

        return ResponseEntity.ok().body(memberProfile);
    }

    @PostMapping("/profile/update/send-verification-code")
    public ResponseEntity<String> sendUpdateEmailVerificationCode(@Valid @RequestBody EmailDto dto) {
        sendUpdateLocalAccountEmailVerificationCodeUseCase.sendUpdateLocalAccountEmailVerificationCode(
            new Email(dto.getEmail()));

        return ResponseEntity.ok().body("전송 완료");
    }

    @PostMapping("/profile/update/validate-verification-code")
    public ResponseEntity<String> validateRegisterVerificationCode(
        @Valid @RequestBody CheckLocalAccountEmailVerificationCodeRequest request
    ) {
        CheckLocalAccountEmailVerificationCodeCommand command = request.toCommand(EmailVerificationPurpose.UPDATE);

        if (checkEmailVerificationCodeQuery.checkEmailVerificationCode(command)) {
            return ResponseEntity.ok().body("인증 성공");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
    }

    @PutMapping("/profile/update")
    public ResponseEntity<String> updateMemberProfile(
        @AuthenticationPrincipal MemberPrincipal principal,
        @Valid @RequestBody UpdateMemberProfileWebRequest request
    ) {
        updateMemberProfileUseCase.updateMemberProfile(request.toCommand(principal.getMemberId()));
        return ResponseEntity.ok().body("프로필 수정 완료");
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdrawMember(@AuthenticationPrincipal MemberPrincipal principal) {
        withdrawMemberUseCase.withdrawMember(principal.getMemberId());
        return ResponseEntity.ok().body("회원 탈퇴 완료");
    }
}
