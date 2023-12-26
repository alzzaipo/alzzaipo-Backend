package com.alzzaipo.member.adapter.out.persistence.account.social;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import com.alzzaipo.member.application.port.out.account.social.DeleteSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.FindSocialAccountPort;
import com.alzzaipo.member.application.port.out.account.social.RegisterSocialAccountPort;
import com.alzzaipo.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
@RequiredArgsConstructor
public class SocialAccountPersistenceAdapter implements RegisterSocialAccountPort,
	FindSocialAccountPort,
	DeleteSocialAccountPort {

	private final MemberRepository memberRepository;
	private final SocialAccountRepository socialAccountRepository;

	@Override
	public void registerSocialAccount(SocialAccount socialAccount) {
		MemberJpaEntity memberJpaEntity = memberRepository.findEntityById(socialAccount.getMemberId().get());
		SocialAccountJpaEntity socialAccountJpaEntity = toJpaEntity(memberJpaEntity, socialAccount);
		socialAccountRepository.save(socialAccountJpaEntity);
	}

	@Override
	public Optional<SocialAccount> findSocialAccount(FindSocialAccountCommand command) {
		return socialAccountRepository.findByLoginTypeAndEmail(command.getLoginType().name(), command.getEmail().get())
			.map(this::toDomainEntity);
	}

	@Override
	public void deleteSocialAccount(Id memberId, LoginType loginType) {
		SocialAccountJpaEntity socialAccountJpaEntity =
			socialAccountRepository.findByMemberJpaEntityIdAndLoginType(memberId.get(), loginType.name())
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "계정 조회 실패"));

		socialAccountRepository.delete(socialAccountJpaEntity);
	}

	private SocialAccountJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity, SocialAccount socialAccount) {
		return new SocialAccountJpaEntity(socialAccount.getEmail().get(), socialAccount.getLoginType().name(),
			memberJpaEntity);
	}

	private SocialAccount toDomainEntity(SocialAccountJpaEntity socialAccountJpaEntity) {
		Id memberId = new Id(socialAccountJpaEntity.getMemberJpaEntity().getId());
		Email email = new Email(socialAccountJpaEntity.getEmail());
		LoginType loginType = LoginType.valueOf(socialAccountJpaEntity.getLoginType());
		return new SocialAccount(memberId, email, loginType);
	}
}
