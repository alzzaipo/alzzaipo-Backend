package com.alzzaipo.domain.account.social;

import com.alzzaipo.domain.BaseTimeEntity;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.enums.LoginType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "social_account", uniqueConstraints = {
        @UniqueConstraint(name = "uix-email-login_type", columnNames = {"email", "login_type"})
})
public class SocialAccount extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 256)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name="login_type", nullable = false)
    private LoginType loginType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public SocialAccount(String email, LoginType loginType, Member member) {
        this.email = email;
        this.loginType = loginType;
        this.member = member;
    }
}
