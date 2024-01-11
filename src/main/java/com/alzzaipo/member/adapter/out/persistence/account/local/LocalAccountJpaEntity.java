package com.alzzaipo.member.adapter.out.persistence.account.local;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "local_account")
public class LocalAccountJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String accountId;

    @Column(nullable = false)
    private String accountPassword;

    @Column(unique = true, nullable = false, length = 256)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberJpaEntity memberJpaEntity;

    public LocalAccountJpaEntity(String accountId, String accountPassword, String email,
        MemberJpaEntity memberJpaEntity) {
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.email = email;
        this.memberJpaEntity = memberJpaEntity;
    }

    public void changePassword(String newPassword) {
        this.accountPassword = newPassword;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public static LocalAccountJpaEntity build(MemberJpaEntity memberJpaEntity, SecureLocalAccount secureLocalAccount) {
        return new LocalAccountJpaEntity(
            secureLocalAccount.getAccountId().get(),
            secureLocalAccount.getEncryptedAccountPassword(),
            secureLocalAccount.getEmail().get(),
            memberJpaEntity);
    }

    public SecureLocalAccount toDomainEntity() {
        return new SecureLocalAccount(
            new com.alzzaipo.common.Id(memberJpaEntity.getId()),
            new LocalAccountId(accountId),
            accountPassword,
            new Email(email));
    }
}
