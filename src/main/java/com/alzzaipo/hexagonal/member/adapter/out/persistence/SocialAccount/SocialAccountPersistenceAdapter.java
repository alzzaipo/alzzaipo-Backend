package com.alzzaipo.hexagonal.member.adapter.out.persistence.SocialAccount;

import com.alzzaipo.hexagonal.member.adapter.out.persistence.Member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.Member.NewMemberRepository;
import com.alzzaipo.hexagonal.member.application.port.out.RegisterSocialAccountPort;
import com.alzzaipo.hexagonal.member.domain.SocialAccount.SocialAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class SocialAccountPersistenceAdapter implements RegisterSocialAccountPort {

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

    private SocialAccountJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity, SocialAccount socialAccount) {
        return new SocialAccountJpaEntity(
                socialAccount.getEmail().get(),
                socialAccount.getLoginType(),
                memberJpaEntity);
    }
}
