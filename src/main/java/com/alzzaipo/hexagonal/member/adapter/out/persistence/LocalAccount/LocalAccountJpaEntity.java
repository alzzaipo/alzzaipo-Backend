package com.alzzaipo.hexagonal.member.adapter.out.persistence.LocalAccount;

import com.alzzaipo.hexagonal.member.adapter.out.persistence.Member.MemberJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class LocalAccountJpaEntity {

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
    @JoinColumn(name = "uid")
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
}
