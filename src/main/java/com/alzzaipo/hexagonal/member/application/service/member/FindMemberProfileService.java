package com.alzzaipo.hexagonal.member.application.service.member;

import com.alzzaipo.hexagonal.common.LoginType;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.in.FindMemberProfileQuery;
import com.alzzaipo.hexagonal.member.application.port.in.dto.MemberProfile;
import com.alzzaipo.hexagonal.member.application.port.in.dto.MemberType;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByMemberUidPort;
import com.alzzaipo.hexagonal.member.application.port.out.FindMemberPort;
import com.alzzaipo.hexagonal.member.application.port.out.FindMemberSocialAccountsPort;
import com.alzzaipo.hexagonal.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.hexagonal.member.domain.Member.Member;
import com.alzzaipo.hexagonal.member.domain.SocialAccount.SocialAccount;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        Optional<SecureLocalAccount> localAccount
                = findLocalAccountByMemberUidPort.findLocalAccountByMemberUid(memberUID);

        if (localAccount.isPresent()) {
            return findLocalMemberProfile(member, localAccount.get(), currentLoginType);
        }
        return findSocialMemberProfile(member, currentLoginType);
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
                .orElseThrow(() -> new RuntimeException("소셜 계정 조회 실패"));

        return new MemberProfile(
                MemberType.SOCIAL,
                null,
                member.getNickname(),
                currentSocialAccount.getEmail().get(),
                null,
                currentLoginType);
    }
}
