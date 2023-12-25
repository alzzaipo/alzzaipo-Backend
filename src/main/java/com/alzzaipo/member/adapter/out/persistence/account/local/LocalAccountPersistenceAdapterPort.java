package com.alzzaipo.member.adapter.out.persistence.account.local;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountEmailPort;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountPasswordPort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountEmailAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountIdAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByAccountIdPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.account.local.RegisterLocalAccountPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class LocalAccountPersistenceAdapterPort implements FindLocalAccountByAccountIdPort,
	RegisterLocalAccountPort,
	FindLocalAccountByMemberUidPort,
	ChangeLocalAccountPasswordPort,
	ChangeLocalAccountEmailPort,
	CheckLocalAccountIdAvailablePort,
	CheckLocalAccountEmailAvailablePort {

	private final MemberRepository memberRepository;
	private final LocalAccountRepository localAccountRepository;

	@Override
	@Transactional(readOnly = true)
	public Optional<SecureLocalAccount> findLocalAccountByAccountId(LocalAccountId localAccountId) {
		return localAccountRepository.findLocalAccountJpaEntityByAccountId(localAccountId.get())
			.map(this::toSecureLocalAccount);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<SecureLocalAccount> findByMemberId(Uid memberUID) {
		return localAccountRepository.findLocalAccountJpaEntityByMemberJpaEntityUid(memberUID.get())
			.map(this::toSecureLocalAccount);
	}

	@Override
	public void registerLocalAccountPort(SecureLocalAccount secureLocalAccount) {
		MemberJpaEntity memberJpaEntity = memberRepository.findEntityById(secureLocalAccount.getMemberUID().get());
		LocalAccountJpaEntity localAccountJpaEntity = toJpaEntity(memberJpaEntity, secureLocalAccount);
		localAccountRepository.save(localAccountJpaEntity);
	}

	@Override
	public boolean changeLocalAccountPassword(LocalAccountId accountId, String encryptedNewAccountPassword) {
		Optional<LocalAccountJpaEntity> localAccountJpaEntity
			= localAccountRepository.findLocalAccountJpaEntityByAccountId(accountId.get());

		localAccountJpaEntity.ifPresent(entity -> entity.changePassword(encryptedNewAccountPassword));

		return localAccountJpaEntity.isPresent();
	}

	@Override
	public void changeLocalAccountEmail(LocalAccountId localAccountId, Email email) {
		localAccountRepository.findLocalAccountJpaEntityByAccountId(localAccountId.get())
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
		Uid memberUID = new Uid(jpaEntity.getMemberJpaEntity().getUid());
		LocalAccountId localAccountId = new LocalAccountId(jpaEntity.getAccountId());
		String encryptedLocalAccountPassword = jpaEntity.getAccountPassword();
		Email email = new Email(jpaEntity.getEmail());
		return new SecureLocalAccount(memberUID, localAccountId, encryptedLocalAccountPassword, email);
	}

	private LocalAccountJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity, SecureLocalAccount secureLocalAccount) {
		String accountId = secureLocalAccount.getAccountId().get();
		String encryptedAccountPassword = secureLocalAccount.getEncryptedAccountPassword();
		String email = secureLocalAccount.getEmail().get();
		return new LocalAccountJpaEntity(accountId, encryptedAccountPassword, email, memberJpaEntity);
	}
}
