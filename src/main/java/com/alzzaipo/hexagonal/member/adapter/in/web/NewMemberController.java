package com.alzzaipo.hexagonal.member.adapter.in.web;

import com.alzzaipo.hexagonal.member.application.port.in.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.hexagonal.member.application.port.in.RegisterLocalAccountCommand;
import com.alzzaipo.hexagonal.member.application.port.in.RegisterLocalAccountUseCase;
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

    @GetMapping("/verify-account-id")
    public ResponseEntity<String> verifyAccountId(@RequestParam("accountId") String accountId) {
        LocalAccountId localAccountId = new LocalAccountId(accountId);

        if (!checkLocalAccountIdAvailabilityQuery.checkLocalAccountIdAvailability(localAccountId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 아이디 입니다.");
        }

        return ResponseEntity.ok().body("사용 가능한 아이디 입니다.");
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
}
