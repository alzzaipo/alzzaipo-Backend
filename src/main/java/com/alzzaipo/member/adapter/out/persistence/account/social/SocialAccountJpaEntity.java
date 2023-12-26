package com.alzzaipo.member.adapter.out.persistence.account.social;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "social_account_jpa_entity",
        uniqueConstraints = {@UniqueConstraint(name = "uidx-email-login_type", columnNames = {"email", "login_type"})})
public class SocialAccountJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 256)
    private String email;

    @Column(name = "login_type", nullable = false)
    private String loginType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberJpaEntity memberJpaEntity;

    public SocialAccountJpaEntity(String email, String loginType, MemberJpaEntity memberJpaEntity) {
        this.email = email;
        this.loginType = loginType;
        this.memberJpaEntity = memberJpaEntity;
    }
}
