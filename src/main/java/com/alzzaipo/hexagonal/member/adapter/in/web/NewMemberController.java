package com.alzzaipo.hexagonal.member.adapter.in.web;

import com.alzzaipo.hexagonal.member.application.port.in.RegisterLocalAccountCommand;
import com.alzzaipo.hexagonal.member.application.port.in.RegisterLocalAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/new/member")
@RequiredArgsConstructor
public class NewMemberController {

    private final RegisterLocalAccountUseCase registerLocalAccountUseCase;

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
