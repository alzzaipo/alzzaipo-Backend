package com.alzzaipo.hexagonal.member.adapter.out.persistence.member;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.out.member.ChangeMemberNicknamePort;
import com.alzzaipo.hexagonal.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.hexagonal.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.hexagonal.member.application.port.out.member.WithdrawMemberPort;
import com.alzzaipo.hexagonal.member.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberPersistenceAdapterPort implements
        RegisterMemberPort,
        FindMemberPort,
        ChangeMemberNicknamePort,
        WithdrawMemberPort {

    private final NewMemberRepository memberRepository;

    @Override
    public void registerMember(Member member) {
        MemberJpaEntity memberJpaEntity = new MemberJpaEntity(member.getUid().get(), member.getNickname());
        memberRepository.save(memberJpaEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findMember(Uid uid) {
        return memberRepository.findByUid(uid.get())
                .map(this::toDomainEntity);
    }

    @Override
    public void changeMemberNickname(Uid memberUID, String nickname) {
        memberRepository.findByUid(memberUID.get())
                .ifPresent(entity -> entity.changeNickname(nickname));
    }

    @Override
    public void withdrawMember(Uid memberUID) {
        MemberJpaEntity entity = memberRepository.findByUid(memberUID.get())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        memberRepository.delete(entity);
    }

    private Member toDomainEntity(MemberJpaEntity memberJpaEntity) {
        return new Member(
                new Uid(memberJpaEntity.getUid()),
                memberJpaEntity.getNickname());
    }
}
