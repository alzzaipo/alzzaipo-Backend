package com.alzzaipo.member.adapter.in.web;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.member.application.port.in.account.social.UnlinkSocialAccountUseCase;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.LoginResult;
import com.alzzaipo.member.application.port.in.dto.UnlinkSocialAccountCommand;
import com.alzzaipo.member.application.port.in.oauth.KakaoLoginUseCase;
import com.alzzaipo.member.application.port.in.oauth.LinkKakaoAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final LinkKakaoAccountUseCase linkKakaoAccountUseCase;
    private final UnlinkSocialAccountUseCase unlinkSocialAccountUseCase;

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
        linkKakaoAccountUseCase.linkKakaoAccount(
                principal.getMemberUID(),
                new AuthorizationCode(authCode));

        return ResponseEntity.ok().body("연동 완료");
    }

    @DeleteMapping("/kakao/unlink")
    public ResponseEntity<String> kakaoUnlink(@AuthenticationPrincipal MemberPrincipal principal) {
        UnlinkSocialAccountCommand command = new UnlinkSocialAccountCommand(
                principal.getMemberUID(),
                LoginType.KAKAO);

        unlinkSocialAccountUseCase.unlinkSocialAccountUseCase(command);

        return ResponseEntity.ok().body("해지 완료");
    }
}
