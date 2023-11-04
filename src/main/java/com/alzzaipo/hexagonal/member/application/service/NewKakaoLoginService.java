package com.alzzaipo.hexagonal.member.application.service;

import com.alzzaipo.hexagonal.common.LoginType;
import com.alzzaipo.hexagonal.common.jwt.NewJwtUtil;
import com.alzzaipo.hexagonal.member.application.port.in.KakaoLoginUseCase;
import com.alzzaipo.hexagonal.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.hexagonal.member.application.port.in.dto.LoginResult;
import com.alzzaipo.hexagonal.member.application.port.out.*;
import com.alzzaipo.hexagonal.member.application.port.out.dto.AccessToken;
import com.alzzaipo.hexagonal.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.hexagonal.member.application.port.out.dto.UserProfile;
import com.alzzaipo.hexagonal.member.domain.Member.Member;
import com.alzzaipo.hexagonal.member.domain.SocialAccount.SocialAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewKakaoLoginService implements KakaoLoginUseCase {

    private final static LoginType LOGIN_TYPE = LoginType.KAKAO;

    private final ExchangeKakaoAccessTokenPort exchangeKakaoAccessTokenPort;
    private final FetchKakaoUserProfilePort fetchKakaoUserProfilePort;
    private final FindSocialAccountPort findSocialAccountPort;
    private final RegisterMemberPort registerMemberPort;
    private final RegisterSocialAccountPort registerSocialAccountPort;

    private final NewJwtUtil jwtUtil;

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
