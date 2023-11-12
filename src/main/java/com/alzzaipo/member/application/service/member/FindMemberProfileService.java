package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.dto.MemberProfile;
import com.alzzaipo.member.application.port.in.dto.MemberType;
import com.alzzaipo.member.application.port.in.member.FindMemberProfileQuery;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.member.application.port.out.member.FindMemberSocialAccountsPort;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import com.alzzaipo.member.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindMemberProfileService implements FindMemberProfileQuery {

    private final FindMemberPort findMemberPort;
    private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;
    private final FindMemberSocialAccountsPort findMemberSocialAccountsPort;

    @Override
    public MemberProfile findMemberProfile(Uid memberUID, LoginType currentLoginType) {
        Member member = findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));

        Optional<SecureLocalAccount> optionalLocalAccount
                = findLocalAccountByMemberUidPort.findLocalAccountByMemberUid(memberUID);

        return optionalLocalAccount
                .map(localAccount -> findLocalMemberProfile(member, localAccount, currentLoginType))
                .orElseGet(() -> findSocialMemberProfile(member, currentLoginType));
    }

    private MemberProfile findLocalMemberProfile(Member member, SecureLocalAccount localAccount, LoginType currentLoginType) {
        List<LoginType> linkedLoginTypes = findMemberSocialAccountsPort.findMemberSocialAccounts(member.getUid())
                .stream()
                .map(SocialAccount::getLoginType)
                .collect(Collectors.toList());

        return new MemberProfile(
                MemberType.LOCAL,
                localAccount.getAccountId().get(),
                member.getNickname(),
                localAccount.getEmail().get(),
                linkedLoginTypes,
                currentLoginType);
    }

    private MemberProfile findSocialMemberProfile(Member member, LoginType currentLoginType) {
        SocialAccount currentSocialAccount = findMemberSocialAccountsPort.findMemberSocialAccounts(member.getUid())
                .stream()
                .filter(socialAccount -> socialAccount.getLoginType() == currentLoginType)
                .findAny()
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "소셜 계정 조회 실패"));

        return new MemberProfile(
                MemberType.SOCIAL,
                null,
                member.getNickname(),
                currentSocialAccount.getEmail().get(),
                null,
                currentLoginType);
    }
}
