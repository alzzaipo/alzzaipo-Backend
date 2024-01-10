package com.alzzaipo.member.adapter.out.persistence.account.social;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "social_account",
        uniqueConstraints = {@UniqueConstraint(name = "uix-email-login_type", columnNames = {"email", "login_type"})})
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

    public SocialAccount toDomainEntity() {
        return new SocialAccount(
            new com.alzzaipo.common.Id(memberJpaEntity.getId()),
            new Email(email),
            LoginType.valueOf(loginType));
    }

    public static SocialAccountJpaEntity build(MemberJpaEntity memberJpaEntity, SocialAccount socialAccount) {
        return new SocialAccountJpaEntity(
            socialAccount.getEmail().get(),
            socialAccount.getLoginType().name(),
            memberJpaEntity);
    }

}
