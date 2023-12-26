package com.alzzaipo.member.adapter.out.persistence.member;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.adapter.out.persistence.account.local.LocalAccountJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.account.social.SocialAccountJpaEntity;
import com.alzzaipo.member.application.port.in.dto.MemberProfile;
import com.alzzaipo.member.application.port.in.dto.MemberType;
import com.alzzaipo.member.application.port.out.member.ChangeMemberNicknamePort;
import com.alzzaipo.member.application.port.out.member.FindMemberAccountEmailsPort;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.member.application.port.out.member.FindMemberProfilePort;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.application.port.out.member.WithdrawMemberPort;
import com.alzzaipo.member.domain.member.Member;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements RegisterMemberPort,
	FindMemberPort,
	ChangeMemberNicknamePort,
	WithdrawMemberPort,
	FindMemberAccountEmailsPort,
	FindMemberProfilePort {

	private final MemberRepository memberRepository;

	@Override
	public void registerMember(Member member) {
		MemberJpaEntity memberJpaEntity = new MemberJpaEntity(member.getId().get(),
			member.getNickname());
		memberRepository.save(memberJpaEntity);
	}

	@Override
	public Member findMember(Id id) throws CustomException {
		MemberJpaEntity entity = memberRepository.findEntityById(id.get());
		return toDomainEntity(entity);
	}

	@Override
	public void changeMemberNickname(Id memberId, String nickname) {
		MemberJpaEntity entity = memberRepository.findEntityById(memberId.get());
		entity.changeNickname(nickname);
	}

	@Override
	public void withdrawMember(Id memberId) {
		memberRepository.deleteById(memberId.get());
	}

	@Override
	public Set<String> findEmails(Id memberId) {
		Set<String> emails = new HashSet<>();

		MemberJpaEntity entity = memberRepository.findEntityById(memberId.get());
		if (entity.getLocalAccountJpaEntity() != null) {
			emails.add(entity.getLocalAccountJpaEntity().getEmail());
		}
		entity.getSocialAccountJpaEntities().forEach(account -> emails.add(account.getEmail()));

		return emails;
	}

	@Override
	public MemberProfile findProfile(long memberId, LoginType currentLoginType) {
		MemberJpaEntity member = memberRepository.findEntityById(memberId);

		if(member.getLocalAccountJpaEntity() != null) {
			return getLocalAccountProfile(member, currentLoginType);
		}
		return getSocialAccountProfile(member, currentLoginType);
	}

	private Member toDomainEntity(MemberJpaEntity memberJpaEntity) {
		return new Member(new Id(memberJpaEntity.getId()),
			memberJpaEntity.getNickname(),
			memberJpaEntity.getRole());
	}

	private MemberProfile getLocalAccountProfile(MemberJpaEntity member, LoginType currentLoginType) {
		LocalAccountJpaEntity localAccount = member.getLocalAccountJpaEntity();

		List<LoginType> linkedLoginTypes = member.getSocialAccountJpaEntities()
			.stream()
			.map(socialAccount -> LoginType.valueOf(socialAccount.getLoginType()))
			.collect(Collectors.toList());

		return new MemberProfile(MemberType.LOCAL,
			localAccount.getAccountId(),
			member.getNickname(),
			localAccount.getEmail(),
			linkedLoginTypes,
			currentLoginType);
	}

	private MemberProfile getSocialAccountProfile(MemberJpaEntity member, LoginType currentLoginType) {
		SocialAccountJpaEntity currentLoginSocialAccount = member.getSocialAccountJpaEntities()
			.stream()
			.filter(socialAccount -> socialAccount.getLoginType().equals(currentLoginType.name()))
			.findAny()
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "프로필 조회 실패"));

		return new MemberProfile(MemberType.SOCIAL,
			null,
			member.getNickname(),
			currentLoginSocialAccount.getEmail(),
			null,
			currentLoginType);
	}
}
