package com.alzzaipo.hexagonal.member.adapter.out.persistence.Member;

import com.alzzaipo.hexagonal.member.application.port.out.RegisterMemberPort;
import com.alzzaipo.hexagonal.member.domain.Member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements RegisterMemberPort {

    private final NewMemberRepository memberRepository;

    @Override
    public void registerMember(Member member) {
        MemberJpaEntity memberJpaEntity = new MemberJpaEntity(member.getUid().get(), member.getNickname());
        memberRepository.save(memberJpaEntity);
    }
}
