package com.alzzaipo.common.token.adapter.in.web;

import com.alzzaipo.common.token.domain.TokenInfo;
import com.alzzaipo.common.token.application.port.in.RefreshTokenUseCase;
import com.alzzaipo.member.adapter.in.web.dto.RefreshTokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/login/refresh-token")
    public ResponseEntity<TokenInfo> refreshToken(@Valid @RequestBody RefreshTokenDto dto) {
        TokenInfo tokenInfo = refreshTokenUseCase.refresh(dto.getRefreshToken());
        return ResponseEntity.ok(tokenInfo);
    }
}
