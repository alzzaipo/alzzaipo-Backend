package com.alzzaipo.member.adapter.out.persistence.account.local;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.Uid;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountEmail;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountPasswordPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByAccountIdPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByEmailPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.account.local.RegisterLocalAccountPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class LocalAccountPersistenceAdapter implements
        FindLocalAccountByAccountIdPort,
        FindLocalAccountByEmailPort,
        RegisterLocalAccountPort,
        FindLocalAccountByMemberUidPort,
        ChangeLocalAccountPasswordPort,
        ChangeLocalAccountEmail {

    private final EntityManager entityManager;

    private final LocalAccountRepository localAccountRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<SecureLocalAccount> findLocalAccountByAccountId(LocalAccountId localAccountId) {
        return localAccountRepository.findByAccountId(localAccountId.get())
                .map(this::toSecureLocalAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecureLocalAccount> findLocalAccountByEmailPort(Email email) {
        return localAccountRepository.findByEmail(email.get())
                .map(this::toSecureLocalAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecureLocalAccount> findLocalAccountByMemberUid(Uid memberUID) {
        return localAccountRepository.findByMemberUID(memberUID.get())
                .map(this::toSecureLocalAccount);
    }

    @Override
    public void registerLocalAccountPort(SecureLocalAccount secureLocalAccount) {
        MemberJpaEntity memberJpaEntity
            = memberRepository.findEntityById(secureLocalAccount.getMemberUID().get());

        LocalAccountJpaEntity localAccountJpaEntity = toJpaEntity(memberJpaEntity, secureLocalAccount);

        localAccountRepository.save(localAccountJpaEntity);
    }

    @Override
    public boolean changeLocalAccountPassword(LocalAccountId accountId, String encryptedNewAccountPassword) {
        Optional<LocalAccountJpaEntity> optionalLocalAccountJpaEntity
                = localAccountRepository.findByAccountId(accountId.get());

        optionalLocalAccountJpaEntity.ifPresent(entity -> {
            entity.changePassword(encryptedNewAccountPassword);
            entityManager.flush();
        });

        return optionalLocalAccountJpaEntity.isPresent();
    }

    @Override
    public void changeLocalAccountEmail(LocalAccountId localAccountId, Email email) {
        localAccountRepository.findByAccountId(localAccountId.get())
                .ifPresent(entity -> entity.changeEmail(email.get()));
    }

    private SecureLocalAccount toSecureLocalAccount(LocalAccountJpaEntity jpaEntity) {
        Uid memberUID = new Uid(jpaEntity.getMemberJpaEntity().getUid());
        LocalAccountId localAccountId = new LocalAccountId(jpaEntity.getAccountId());
        String encryptedLocalAccountPassword = jpaEntity.getAccountPassword();
        Email email = new Email(jpaEntity.getEmail());

        return new SecureLocalAccount(
                memberUID,
                localAccountId,
                encryptedLocalAccountPassword,
                email);
    }

    private LocalAccountJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity, SecureLocalAccount secureLocalAccount) {
        String accountId = secureLocalAccount.getAccountId().get();
        String encryptedAccountPassword = secureLocalAccount.getEncryptedAccountPassword();
        String email = secureLocalAccount.getEmail().get();

        return new LocalAccountJpaEntity(
                accountId,
                encryptedAccountPassword,
                email,
                memberJpaEntity);
    }
}
