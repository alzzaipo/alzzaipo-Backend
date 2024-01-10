package com.alzzaipo.member.application.service.account;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.common.token.TokenUtil;
import com.alzzaipo.common.token.application.port.out.SaveRefreshTokenPort;
import com.alzzaipo.common.token.domain.TokenInfo;
import com.alzzaipo.member.application.port.in.account.social.KakaoLoginUseCase;
import com.alzzaipo.member.application.port.in.account.social.LinkKakaoAccountUseCase;
import com.alzzaipo.member.application.port.in.account.social.UnlinkSocialAccountUseCase;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.LinkKakaoAccountCommand;
import com.alzzaipo.member.application.port.in.dto.LoginResult;
import com.alzzaipo.member.application.port.in.dto.UnlinkSocialAccountCommand;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberIdPort;
import com.alzzaipo.member.application.port.out.account.social.DeleteSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.ExchangeKakaoAccessTokenPort;
import com.alzzaipo.member.application.port.out.account.social.FetchKakaoUserProfilePort;
import com.alzzaipo.member.application.port.out.account.social.FindSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.RegisterSocialAccountPort;
import com.alzzaipo.member.application.port.out.dto.AccessToken;
import com.alzzaipo.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.member.application.port.out.dto.UserProfile;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import com.alzzaipo.member.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialAccountService implements LinkKakaoAccountUseCase,
    UnlinkSocialAccountUseCase,
    KakaoLoginUseCase {

    private final FindLocalAccountByMemberIdPort findLocalAccountByMemberIdPort;
    private final ExchangeKakaoAccessTokenPort exchangeKakaoAccessTokenPort;
    private final FetchKakaoUserProfilePort fetchKakaoUserProfilePort;
    private final FindSocialAccountPort findSocialAccountPort;
    private final RegisterSocialAccountPort registerSocialAccountPort;
    private final DeleteSocialAccountPort deleteSocialAccountUsePort;
    private final SaveRefreshTokenPort saveRefreshTokenPort;
    private final RegisterMemberPort registerMemberPort;

    @Override
    public void linkKakaoAccount(LinkKakaoAccountCommand command) {
        findLocalAccountByMemberIdPort.findByMemberId(command.getMemberId())
            .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "로컬 계정에서만 가능합니다"));

        AccessToken accessToken = exchangeKakaoAccessTokenPort.exchangeKakaoAccessToken(command.getAuthorizationCode());

        UserProfile userProfile = fetchKakaoUserProfilePort.fetchKakaoUserProfile(accessToken);

        checkLinkedKakaoAccountExists(userProfile);

        registerSocialAccountPort.registerSocialAccount(
            new SocialAccount(command.getMemberId(), userProfile.getEmail(), LoginType.KAKAO));
    }

    @Override
    public void unlinkSocialAccountUseCase(UnlinkSocialAccountCommand command) {
        findLocalAccountByMemberIdPort.findByMemberId(command.getMemberId())
            .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "로컬 계정에서만 가능합니다"));

        deleteSocialAccountUsePort.deleteSocialAccount(command.getMemberId(), command.getLoginType());
    }

    @Override
    public LoginResult handleLogin(AuthorizationCode authorizationCode) {
        try {
            AccessToken kakaoAccessToken
                = exchangeKakaoAccessTokenPort.exchangeKakaoAccessToken(authorizationCode);

            UserProfile kakaoUserProfile
                = fetchKakaoUserProfilePort.fetchKakaoUserProfile(kakaoAccessToken);

            FindSocialAccountCommand command = new FindSocialAccountCommand(LoginType.KAKAO,
                kakaoUserProfile.getEmail());
            SocialAccount socialAccount = findSocialAccountPort.findSocialAccount(command)
                .orElseGet(() -> registerSocialAccount(kakaoUserProfile));

            TokenInfo tokenInfo = TokenUtil.createToken(socialAccount.getMemberId(), LoginType.KAKAO);

            saveRefreshTokenPort.save(tokenInfo.getRefreshToken(), socialAccount.getMemberId());

            return new LoginResult(true, tokenInfo);
        } catch (Exception e) {
            return LoginResult.getFailedResult();
        }
    }

    private void checkLinkedKakaoAccountExists(UserProfile userProfile) {
        FindSocialAccountCommand findSocialAccountCommand = new FindSocialAccountCommand(
            LoginType.KAKAO, userProfile.getEmail());

        if (findSocialAccountPort.findSocialAccount(findSocialAccountCommand).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 연동된 계정이 존재합니다");
        }
    }

    private SocialAccount registerSocialAccount(UserProfile userProfile) {
        Member member = Member.build(userProfile.getNickname());
        registerMemberPort.registerMember(member);

        SocialAccount socialAccount = new SocialAccount(member.getId(), userProfile.getEmail(), LoginType.KAKAO);
        registerSocialAccountPort.registerSocialAccount(socialAccount);

        return socialAccount;
    }

}
