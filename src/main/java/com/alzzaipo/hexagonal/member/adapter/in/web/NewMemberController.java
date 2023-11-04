package com.alzzaipo.hexagonal.member.adapter.in.web;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.in.*;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class NewMemberController {

    private final RegisterLocalAccountUseCase registerLocalAccountUseCase;
    private final CheckLocalAccountIdAvailabilityQuery checkLocalAccountIdAvailabilityQuery;
    private final CheckLocalAccountEmailAvailabilityQuery checkLocalAccountEmailAvailabilityQuery;
    private final LocalLoginUseCase localLoginUseCase;
    private final CheckLocalAccountPasswordQuery checkLocalAccountPasswordQuery;
    private final ChangeLocalAccountPasswordUseCase changeLocalAccountPasswordUseCase;

    @GetMapping("/verify-account-id")
    public ResponseEntity<String> checkLocalAccountIdAvailability(@RequestParam("accountId") String accountId) {
        LocalAccountId localAccountId = new LocalAccountId(accountId);

        if (!checkLocalAccountIdAvailabilityQuery.checkLocalAccountIdAvailability(localAccountId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 아이디 입니다.");
        }
        return ResponseEntity.ok().body("사용 가능한 아이디 입니다.");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> checkLocalAccountEmailAvailability(@RequestParam("email") String email) {
        Email localAccountEmail = new Email(email);

        if (!checkLocalAccountEmailAvailabilityQuery.checkLocalAccountEmailAvailability(localAccountEmail)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 이메일 입니다.");
        }
        return ResponseEntity.ok().body("사용 가능한 이메일 입니다.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterLocalAccountWebRequest dto) {
        RegisterLocalAccountCommand command = new RegisterLocalAccountCommand(
                dto.getAccountId(),
                dto.getAccountPassword(),
                dto.getEmail(),
                dto.getNickname());

        registerLocalAccountUseCase.registerLocalAccount(command);

        return ResponseEntity.ok().body("가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LocalLoginWebRequest dto) {
        LocalLoginCommand localLoginCommand = new LocalLoginCommand(
                dto.getAccountId(),
                dto.getAccountPassword());

        LocalLoginResult localLoginResult = localLoginUseCase.handleLocalLogin(localLoginCommand);

        if (localLoginResult.isSuccess()) {
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + localLoginResult.getToken())
                    .body("로그인 성공");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
    }

    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@AuthenticationPrincipal MemberPrincipal principal, @RequestBody LocalAccountPasswordDto dto) {
        Uid memberUID = principal.getMemberUID();
        LocalAccountPassword localAccountPassword = new LocalAccountPassword(dto.getAccountPassword());

        if (checkLocalAccountPasswordQuery.checkLocalAccountPassword(memberUID, localAccountPassword)) {
            return ResponseEntity.ok().body("비밀번호 검증 성공");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 검증 실패");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal MemberPrincipal principal,
            @RequestBody ChangeLocalAccountPasswordWebRequest dto) {
        Uid memberUID = principal.getMemberUID();
        LocalAccountPassword currentAccountPassword = new LocalAccountPassword(dto.getCurrentAccountPassword());
        LocalAccountPassword newAccountPassword = new LocalAccountPassword(dto.getNewAccountPassword());
        ChangeLocalAccountPasswordCommand command = new ChangeLocalAccountPasswordCommand(memberUID, newAccountPassword);

        if (!checkLocalAccountPasswordQuery.checkLocalAccountPassword(memberUID, currentAccountPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 검증 실패");
        }

        if (changeLocalAccountPasswordUseCase.changeLocalAccountPassword(command)) {
            return ResponseEntity.ok().body("비밀번호 변경 완료");
        }
        return ResponseEntity.internalServerError().body("비밀번호 변경 실패");
    }
}
