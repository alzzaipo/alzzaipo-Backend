package com.alzzaipo.member.adapter.out.persistence.account.local;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import jakarta.persistence.*;
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

    public LocalAccountJpaEntity(String accountId, String accountPassword, String email, MemberJpaEntity memberJpaEntity) {
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
}
