package com.alzzaipo.member.adapter.out.persistence.account.social;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.member.application.port.out.member.FindMemberSocialAccountsPort;
import com.alzzaipo.member.application.port.out.account.social.FindSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.RegisterSocialAccountPort;
import com.alzzaipo.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class SocialAccountPersistenceAdapter implements
        RegisterSocialAccountPort,
        FindSocialAccountPort,
        FindMemberSocialAccountsPort {

    private final NewMemberRepository memberRepository;
    private final NewSocialAccountRepository socialAccountRepository;

    @Override
    public boolean registerSocialAccount(SocialAccount socialAccount) {
        Optional<MemberJpaEntity> optionalMemberJpaEntity
                = memberRepository.findByUid(socialAccount.getMemberUID().get());

        optionalMemberJpaEntity.ifPresent(memberJpaEntity -> {
            SocialAccountJpaEntity socialAccountJpaEntity = toJpaEntity(memberJpaEntity, socialAccount);
            socialAccountRepository.save(socialAccountJpaEntity);
        });

        return optionalMemberJpaEntity.isPresent();
    }

    @Override
    public Optional<SocialAccount> findSocialAccount(FindSocialAccountCommand command) {
        LoginType loginType = command.getLoginType();
        String email = command.getEmail().get();

        return socialAccountRepository.findByLoginTypeAndEmail(loginType.name(), email)
                .map(this::toDomainEntity);
    }

    @Override
    public List<SocialAccount> findMemberSocialAccounts(Uid memberUID) {
        return socialAccountRepository.findByMemberUID(memberUID.get())
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    private SocialAccountJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity, SocialAccount socialAccount) {
        return new SocialAccountJpaEntity(
                socialAccount.getEmail().get(),
                socialAccount.getLoginType().name(),
                memberJpaEntity);
    }

    private SocialAccount toDomainEntity(SocialAccountJpaEntity socialAccountJpaEntity) {
        Uid memberUID = new Uid(socialAccountJpaEntity.getMemberJpaEntity().getUid());
        Email email = new Email(socialAccountJpaEntity.getEmail());
        LoginType loginType = LoginType.valueOf(socialAccountJpaEntity.getLoginType());

        return new SocialAccount(memberUID, email, loginType);
    }
}
