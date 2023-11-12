package com.alzzaipo.member.application.service.account.social;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.jwt.JwtUtil;
import com.alzzaipo.member.application.port.in.oauth.KakaoLoginUseCase;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.LoginResult;
import com.alzzaipo.member.application.port.out.account.social.FindSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.RegisterSocialAccountPort;
import com.alzzaipo.member.application.port.out.dto.AccessToken;
import com.alzzaipo.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.member.application.port.out.dto.UserProfile;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.application.port.out.oauth.ExchangeKakaoAccessTokenPort;
import com.alzzaipo.member.application.port.out.oauth.FetchKakaoUserProfilePort;
import com.alzzaipo.member.domain.member.Member;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoLoginService implements KakaoLoginUseCase {

    private final static LoginType LOGIN_TYPE = LoginType.KAKAO;

    private final ExchangeKakaoAccessTokenPort exchangeKakaoAccessTokenPort;
    private final FetchKakaoUserProfilePort fetchKakaoUserProfilePort;
    private final FindSocialAccountPort findSocialAccountPort;
    private final RegisterMemberPort registerMemberPort;
    private final RegisterSocialAccountPort registerSocialAccountPort;

    private final JwtUtil jwtUtil;

    @Override
    public LoginResult handleLogin(AuthorizationCode authorizationCode) {
        LoginResult loginResult;

        try {
            AccessToken accessToken
                    = exchangeKakaoAccessTokenPort.exchangeKakaoAccessToken(authorizationCode);

            UserProfile userProfile
                    = fetchKakaoUserProfilePort.fetchKakaoUserProfile(accessToken);

            FindSocialAccountCommand findSocialAccountCommand
                    = new FindSocialAccountCommand(LOGIN_TYPE, userProfile.getEmail());

            SocialAccount socialAccount = findSocialAccountPort.findSocialAccount(findSocialAccountCommand)
                    .orElseGet(() -> registerSocialAccount(userProfile));

            String token = jwtUtil.createToken(socialAccount.getMemberUID(), LOGIN_TYPE);
            loginResult = new LoginResult(true, token);
        } catch (Exception e) {
            loginResult = LoginResult.getFailedResult();
        }

        return loginResult;
    }

    private SocialAccount registerSocialAccount(UserProfile userProfile) {
        Member member = Member.create(userProfile.getNickname());

        SocialAccount socialAccount = new SocialAccount(
                member.getUid(),
                userProfile.getEmail(),
                LOGIN_TYPE);

        registerMemberPort.registerMember(member);

        registerSocialAccountPort.registerSocialAccount(socialAccount);

        return socialAccount;
    }
}
