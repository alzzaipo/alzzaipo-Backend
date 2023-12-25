package com.alzzaipo.member.application.service.account.social;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.account.social.UnlinkSocialAccountUseCase;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.in.dto.UnlinkSocialAccountCommand;
import com.alzzaipo.member.application.port.in.oauth.LinkKakaoAccountUseCase;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.account.social.DeleteSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.FindSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.RegisterSocialAccountPort;
import com.alzzaipo.member.application.port.out.dto.AccessToken;
import com.alzzaipo.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.member.application.port.out.dto.UserProfile;
import com.alzzaipo.member.application.port.out.oauth.ExchangeKakaoAccessTokenPort;
import com.alzzaipo.member.application.port.out.oauth.FetchKakaoUserProfilePort;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialAccountService implements LinkKakaoAccountUseCase,
    UnlinkSocialAccountUseCase {

    private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;
    private final ExchangeKakaoAccessTokenPort exchangeKakaoAccessTokenPort;
    private final FetchKakaoUserProfilePort fetchKakaoUserProfilePort;
    private final FindSocialAccountPort findSocialAccountPort;
    private final RegisterSocialAccountPort registerSocialAccountPort;
    private final DeleteSocialAccountPort deleteSocialAccountUsePort;

    @Override
    public void linkKakaoAccount(Uid memberUID, AuthorizationCode authorizationCode) {
        findLocalAccountByMemberUidPort.findByMemberId(memberUID)
            .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "로컬 계정에서만 가능합니다"));

        AccessToken accessToken = exchangeKakaoAccessTokenPort.exchangeKakaoAccessToken(authorizationCode);

        UserProfile userProfile = fetchKakaoUserProfilePort.fetchKakaoUserProfile(accessToken);

        checkLinkedKakaoAccountExists(userProfile);

        registerSocialAccountPort.registerSocialAccount(
            new SocialAccount(memberUID, userProfile.getEmail(), LoginType.KAKAO));
    }

    private void checkLinkedKakaoAccountExists(UserProfile userProfile) {
        FindSocialAccountCommand findSocialAccountCommand = new FindSocialAccountCommand(
            LoginType.KAKAO, userProfile.getEmail());

        if (findSocialAccountPort.findSocialAccount(findSocialAccountCommand).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 연동된 계정이 존재합니다");
        }
    }

    @Override
    public void unlinkSocialAccountUseCase(UnlinkSocialAccountCommand command) {
        findLocalAccountByMemberUidPort.findByMemberId(command.getMemberUID())
            .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "로컬 계정에서만 가능합니다"));

        deleteSocialAccountUsePort.deleteSocialAccount(command.getMemberUID(), command.getLoginType());
    }

}
