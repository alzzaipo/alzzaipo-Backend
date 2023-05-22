package com.alzzaipo.domain.account.social;

import com.alzzaipo.domain.BaseTimeEntity;
import com.alzzaipo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "social_account", uniqueConstraints = {
        @UniqueConstraint(name = "uix-email-social_code", columnNames = {"email", "social_code"})
})
public class SocialAccount extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 256)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name="social_code", nullable = false)
    private SocialCode socialCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public SocialAccount(String email, SocialCode socialCode, Member member) {
        this.email = email;
        this.socialCode = socialCode;
        this.member = member;
    }
}
