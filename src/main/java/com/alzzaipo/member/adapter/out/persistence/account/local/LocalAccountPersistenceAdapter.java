package com.alzzaipo.member.adapter.out.persistence.account.local;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountEmailPort;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountPasswordPort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountEmailAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountIdAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByIdPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberIdPort;
import com.alzzaipo.member.application.port.out.account.local.RegisterLocalAccountPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
@RequiredArgsConstructor
public class LocalAccountPersistenceAdapter implements FindLocalAccountByIdPort,
	RegisterLocalAccountPort,
	FindLocalAccountByMemberIdPort,
	ChangeLocalAccountPasswordPort,
	ChangeLocalAccountEmailPort,
	CheckLocalAccountIdAvailablePort,
	CheckLocalAccountEmailAvailablePort {

	private final MemberRepository memberRepository;
	private final LocalAccountRepository localAccountRepository;

	@Override
	public Optional<SecureLocalAccount> findLocalAccountById(LocalAccountId localAccountId) {
		return localAccountRepository.findByAccountId(localAccountId.get())
			.map(this::toSecureLocalAccount);
	}

	@Override
	public Optional<SecureLocalAccount> findByMemberId(Id memberId) {
		return localAccountRepository.findByMemberJpaEntityId(memberId.get())
			.map(this::toSecureLocalAccount);
	}

	@Override
	public void registerLocalAccountPort(SecureLocalAccount secureLocalAccount) {
		MemberJpaEntity memberJpaEntity = memberRepository.findEntityById(secureLocalAccount.getMemberId().get());
		LocalAccountJpaEntity localAccountJpaEntity = toJpaEntity(memberJpaEntity, secureLocalAccount);
		localAccountRepository.save(localAccountJpaEntity);
	}

	@Override
	public void changePassword(long memberId, String password) {
		LocalAccountJpaEntity localAccount = localAccountRepository.findByMemberJpaEntityId(memberId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "계정 조회 실패"));

		localAccount.changePassword(password);
	}

	@Override
	public void changeLocalAccountEmail(LocalAccountId localAccountId, Email email) {
		localAccountRepository.findByAccountId(localAccountId.get())
			.ifPresent(entity -> entity.changeEmail(email.get()));
	}

	@Override
	public boolean checkAccountIdAvailable(String accountId) {
		return !localAccountRepository.existsByAccountId(accountId);
	}

	@Override
	public boolean checkEmailAvailable(String email) {
		return !localAccountRepository.existsByEmail(email);
	}

	private SecureLocalAccount toSecureLocalAccount(LocalAccountJpaEntity jpaEntity) {
		Id memberId = new Id(jpaEntity.getMemberJpaEntity().getId());
		LocalAccountId localAccountId = new LocalAccountId(jpaEntity.getAccountId());
		String encryptedLocalAccountPassword = jpaEntity.getAccountPassword();
		Email email = new Email(jpaEntity.getEmail());
		return new SecureLocalAccount(memberId, localAccountId, encryptedLocalAccountPassword, email);
	}

	private LocalAccountJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity, SecureLocalAccount secureLocalAccount) {
		String accountId = secureLocalAccount.getAccountId().get();
		String encryptedAccountPassword = secureLocalAccount.getEncryptedAccountPassword();
		String email = secureLocalAccount.getEmail().get();
		return new LocalAccountJpaEntity(accountId, encryptedAccountPassword, email, memberJpaEntity);
	}
}
