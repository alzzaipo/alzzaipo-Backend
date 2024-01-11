package com.alzzaipo.member.adapter.in.web;

import com.alzzaipo.member.adapter.in.web.dto.LocalLoginWebRequest;
import com.alzzaipo.member.application.port.in.account.local.LocalLoginUseCase;
import com.alzzaipo.member.application.port.in.account.social.KakaoLoginUseCase;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.LocalLoginCommand;
import com.alzzaipo.member.application.port.in.dto.LoginResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LocalLoginUseCase localLoginUseCase;
    private final KakaoLoginUseCase kakaoLoginUseCase;

    @PostMapping("/local")
    public ResponseEntity<LoginResult> login(
        HttpServletRequest servletRequest,
        @Valid @RequestBody LocalLoginWebRequest loginRequest
    ) {
        LocalLoginCommand command = loginRequest.toCommand(servletRequest.getRemoteAddr());
        LoginResult loginResult = localLoginUseCase.handleLocalLogin(command);
        return buildResponseEntity(loginResult);
    }

    @GetMapping("/kakao")
    public ResponseEntity<LoginResult> kakaoLogin(@RequestParam("code") String authCode) {
        LoginResult loginResult = kakaoLoginUseCase.handleLogin(new AuthorizationCode(authCode));
        return buildResponseEntity(loginResult);
    }

    private ResponseEntity<LoginResult> buildResponseEntity(LoginResult loginResult) {
        if (loginResult.isSuccess()) {
            return ResponseEntity.ok(loginResult);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResult);
    }
}
