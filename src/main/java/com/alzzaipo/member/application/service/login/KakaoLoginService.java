package com.alzzaipo.member.application.service.login;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.jwt.JwtUtil;
import com.alzzaipo.common.jwt.TokenInfo;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.LoginResult;
import com.alzzaipo.member.application.port.in.oauth.KakaoLoginUseCase;
import com.alzzaipo.member.application.port.out.SaveRefreshTokenPort;
import com.alzzaipo.member.application.port.out.account.social.FindSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.RegisterSocialAccountPort;
import com.alzzaipo.member.application.port.out.dto.AccessToken;
import com.alzzaipo.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.member.application.port.out.dto.UserProfile;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.application.port.out.oauth.ExchangeKakaoAccessTokenPort;
import com.alzzaipo.member.application.port.out.oauth.FetchKakaoUserProfilePort;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import com.alzzaipo.member.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoLoginService implements KakaoLoginUseCase {

	private final static LoginType KAKAO_LOGIN_TYPE = LoginType.KAKAO;

	private final ExchangeKakaoAccessTokenPort exchangeKakaoAccessTokenPort;
	private final FetchKakaoUserProfilePort fetchKakaoUserProfilePort;
	private final FindSocialAccountPort findSocialAccountPort;
	private final RegisterMemberPort registerMemberPort;
	private final RegisterSocialAccountPort registerSocialAccountPort;
	private final SaveRefreshTokenPort saveRefreshTokenPort;

	@Override
	public LoginResult handleLogin(AuthorizationCode authorizationCode) {
		try {
			AccessToken kakaoAccessToken
				= exchangeKakaoAccessTokenPort.exchangeKakaoAccessToken(authorizationCode);

			UserProfile kakaoUserProfile
				= fetchKakaoUserProfilePort.fetchKakaoUserProfile(kakaoAccessToken);

			FindSocialAccountCommand command = new FindSocialAccountCommand(KAKAO_LOGIN_TYPE, kakaoUserProfile.getEmail());
			SocialAccount socialAccount = findSocialAccountPort.findSocialAccount(command)
				.orElseGet(() -> registerSocialAccount(kakaoUserProfile));

			TokenInfo tokenInfo = JwtUtil.createToken(socialAccount.getMemberUID(), KAKAO_LOGIN_TYPE);
			saveRefreshTokenPort.save(tokenInfo.getRefreshToken(), socialAccount.getMemberUID());

			return new LoginResult(true, tokenInfo);
		} catch (Exception e) {
			return LoginResult.getFailedResult();
		}
	}

	private SocialAccount registerSocialAccount(UserProfile userProfile) {
		Member member = Member.create(userProfile.getNickname());
		registerMemberPort.registerMember(member);

		SocialAccount socialAccount = new SocialAccount(member.getUid(), userProfile.getEmail(), KAKAO_LOGIN_TYPE);
		registerSocialAccountPort.registerSocialAccount(socialAccount);

		return socialAccount;
	}
}
