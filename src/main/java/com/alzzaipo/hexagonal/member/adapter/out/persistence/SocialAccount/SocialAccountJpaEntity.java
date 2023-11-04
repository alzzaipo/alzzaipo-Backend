package com.alzzaipo.hexagonal.member.adapter.out.persistence.SocialAccount;

import com.alzzaipo.hexagonal.common.LoginType;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.Member.MemberJpaEntity;
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberJpaEntity memberJpaEntity;

    public SocialAccountJpaEntity(String email, LoginType loginType, MemberJpaEntity memberJpaEntity) {
        this.email = email;
        this.loginType = loginType;
        this.memberJpaEntity = memberJpaEntity;
    }
}
