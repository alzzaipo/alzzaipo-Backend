package com.alzzaipo.member.application.service.account.social;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.in.oauth.LinkKakaoAccountUseCase;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.out.oauth.ExchangeKakaoAccessTokenPort;
import com.alzzaipo.member.application.port.out.oauth.FetchKakaoUserProfilePort;
import com.alzzaipo.member.application.port.out.account.social.FindSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.RegisterSocialAccountPort;
import com.alzzaipo.member.application.port.out.dto.AccessToken;
import com.alzzaipo.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.member.application.port.out.dto.UserProfile;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkKakaoAccountService implements LinkKakaoAccountUseCase {

    private static final LoginType KAKAO_LOGIN_TYPE = LoginType.KAKAO;

    private final ExchangeKakaoAccessTokenPort exchangeKakaoAccessTokenPort;
    private final FetchKakaoUserProfilePort fetchKakaoUserProfilePort;
    private final FindSocialAccountPort findSocialAccountPort;
    private final RegisterSocialAccountPort registerSocialAccountPort;

    @Override
    public boolean linkKakaoAccountUseCase(Uid memberUID, AuthorizationCode authorizationCode) {
        boolean result;

        try {
            AccessToken accessToken = exchangeKakaoAccessTokenPort.exchangeKakaoAccessToken(authorizationCode);

            UserProfile userProfile = fetchKakaoUserProfilePort.fetchKakaoUserProfile(accessToken);

            checkLinkedKakaoAccountExists(userProfile);

            result = registerSocialAccountPort.registerSocialAccount(
                    new SocialAccount(memberUID, userProfile.getEmail(), KAKAO_LOGIN_TYPE));
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    private void checkLinkedKakaoAccountExists(UserProfile userProfile) throws RuntimeException {
        FindSocialAccountCommand findSocialAccountCommand
                = new FindSocialAccountCommand(KAKAO_LOGIN_TYPE, userProfile.getEmail());

        if (findSocialAccountPort.findSocialAccount(findSocialAccountCommand).isPresent()) {
            throw new RuntimeException("이미 연동된 계정이 존재합니다.");
        }
    }
}
