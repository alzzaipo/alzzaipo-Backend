package com.alzzaipo.member.adapter.in.web;

import com.alzzaipo.member.adapter.in.web.dto.LocalLoginWebRequest;
import com.alzzaipo.member.adapter.in.web.dto.TokenResponse;
import com.alzzaipo.member.application.port.in.account.local.LocalLoginUseCase;
import com.alzzaipo.member.application.port.in.account.social.KakaoLoginUseCase;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.LoginResult;
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
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LocalLoginWebRequest request) {
        LoginResult loginResult = localLoginUseCase.handleLocalLogin(request.toCommand());

        if (loginResult.isSuccess()) {
            TokenResponse tokenResponse = TokenResponse.build(loginResult.getTokenInfo());
            return ResponseEntity.ok(tokenResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@RequestParam("code") String authCode) {
        LoginResult loginResult = kakaoLoginUseCase.handleLogin(new AuthorizationCode(authCode));

        if (loginResult.isSuccess()) {
            TokenResponse tokenResponse = TokenResponse.build(loginResult.getTokenInfo());
            return ResponseEntity.ok(tokenResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
