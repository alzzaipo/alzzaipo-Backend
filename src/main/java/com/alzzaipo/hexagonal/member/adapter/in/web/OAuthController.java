package com.alzzaipo.hexagonal.member.adapter.in.web;

import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.in.oauth.KakaoLoginUseCase;
import com.alzzaipo.hexagonal.member.application.port.in.oauth.LinkKakaoAccountUseCase;
import com.alzzaipo.hexagonal.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.hexagonal.member.application.port.in.dto.LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final LinkKakaoAccountUseCase linkKakaoAccountUseCase;

    @GetMapping("/kakao/login")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String authCode) {
        AuthorizationCode authorizationCode = new AuthorizationCode(authCode);

        LoginResult loginResult = kakaoLoginUseCase.handleLogin(authorizationCode);

        if (loginResult.isSuccess()) {
            String token = loginResult.getToken();

            return ResponseEntity.ok()
                    .header("Authorization", token)
                    .body("로그인 성공");
        }
        return ResponseEntity.badRequest().body("로그인 실패");
    }

    @GetMapping("/kakao/link")
    public ResponseEntity<String> kakaoLink(@AuthenticationPrincipal MemberPrincipal principal,
                                            @RequestParam("code") String authCode) {
        AuthorizationCode authorizationCode = new AuthorizationCode(authCode);
        Uid memberUID = principal.getMemberUID();

        boolean success = linkKakaoAccountUseCase.linkKakaoAccountUseCase(memberUID, authorizationCode);

        if (success) {
            return ResponseEntity.ok().body("연동 완료");
        }
        return ResponseEntity.badRequest().body("연동 실패");
    }
}
