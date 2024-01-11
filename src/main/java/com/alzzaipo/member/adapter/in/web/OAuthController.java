package com.alzzaipo.member.adapter.in.web;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.member.application.port.in.account.social.UnlinkSocialAccountUseCase;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.LinkKakaoAccountCommand;
import com.alzzaipo.member.application.port.in.dto.UnlinkSocialAccountCommand;
import com.alzzaipo.member.application.port.in.account.social.LinkKakaoAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

	private final LinkKakaoAccountUseCase linkKakaoAccountUseCase;
	private final UnlinkSocialAccountUseCase unlinkSocialAccountUseCase;

	@GetMapping("/kakao/link")
	public ResponseEntity<String> kakaoLink(@AuthenticationPrincipal MemberPrincipal principal,
		@RequestParam("code") String authCode) {
		LinkKakaoAccountCommand command = new LinkKakaoAccountCommand(principal.getMemberId(), new AuthorizationCode(authCode));
		linkKakaoAccountUseCase.linkKakaoAccount(command);
		return ResponseEntity.ok().body("연동 완료");
	}

	@DeleteMapping("/kakao/unlink")
	public ResponseEntity<String> kakaoUnlink(@AuthenticationPrincipal MemberPrincipal principal) {
		UnlinkSocialAccountCommand command = new UnlinkSocialAccountCommand(principal.getMemberId(), LoginType.KAKAO);
		unlinkSocialAccountUseCase.unlinkSocialAccountUseCase(command);
		return ResponseEntity.ok().body("해지 완료");
	}
}
