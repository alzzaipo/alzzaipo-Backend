package com.alzzaipo.hexagonal.member.adapter.in.web;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.member.application.port.in.*;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/new/member")
@RequiredArgsConstructor
public class NewMemberController {

    private final RegisterLocalAccountUseCase registerLocalAccountUseCase;
    private final CheckLocalAccountIdAvailabilityQuery checkLocalAccountIdAvailabilityQuery;
    private final CheckLocalAccountEmailAvailabilityQuery checkLocalAccountEmailAvailabilityQuery;
    private final LocalLoginUseCase localLoginUseCase;

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
}
