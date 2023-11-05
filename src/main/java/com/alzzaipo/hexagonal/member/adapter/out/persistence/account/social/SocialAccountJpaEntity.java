package com.alzzaipo.hexagonal.member.adapter.out.persistence.account.social;

import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.MemberJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "social_account_jpa_entity",
        uniqueConstraints = {@UniqueConstraint(name = "uidx-email-login_type", columnNames = {"email", "login_type"})})
public class SocialAccountJpaEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 256)
    private String email;

    @Column(name = "login_type", nullable = false)
    private String loginType;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberJpaEntity memberJpaEntity;

    public SocialAccountJpaEntity(String email, String loginType, MemberJpaEntity memberJpaEntity) {
        this.email = email;
        this.loginType = loginType;
        this.memberJpaEntity = memberJpaEntity;
    }
}
