package com.alzzaipo.member.adapter.out.persistence.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.out.member.ChangeMemberNicknamePort;
import com.alzzaipo.member.application.port.out.member.FindMemberAccountEmailsPort;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.application.port.out.member.WithdrawMemberPort;
import com.alzzaipo.member.domain.member.Member;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements RegisterMemberPort,
	FindMemberPort,
	ChangeMemberNicknamePort,
	WithdrawMemberPort,
	FindMemberAccountEmailsPort {

	private final MemberRepository memberRepository;

	@Override
	public void registerMember(Member member) {
		MemberJpaEntity memberJpaEntity = new MemberJpaEntity(member.getUid().get(),
			member.getNickname());
		memberRepository.save(memberJpaEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Member findMember(Uid uid) throws CustomException {
		MemberJpaEntity entity = memberRepository.findEntityById(uid.get());
		return toDomainEntity(entity);
	}

	@Override
	public void changeMemberNickname(Uid memberUID, String nickname) {
		MemberJpaEntity entity = memberRepository.findEntityById(memberUID.get());
		entity.changeNickname(nickname);
	}

	@Override
	public void withdrawMember(Uid memberUID) {
		memberRepository.deleteById(memberUID.get());
	}

	private Member toDomainEntity(MemberJpaEntity memberJpaEntity) {
		return new Member(
			new Uid(memberJpaEntity.getUid()),
			memberJpaEntity.getNickname(),
			memberJpaEntity.getRole());
	}

	@Override
	public Set<String> findEmails(Uid memberId) {
		Set<String> emails = new HashSet<>();

		MemberJpaEntity entity = memberRepository.findEntityById(memberId.get());
		if (entity.getLocalAccountJpaEntity() != null) {
			emails.add(entity.getLocalAccountJpaEntity().getEmail());
		}
		entity.getSocialAccountJpaEntities().forEach(account -> emails.add(account.getEmail()));

		return emails;
	}
}
