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
        @UniqueConstraint(name = "idx_email", columnNames = {"email", "socialCode"})
})
public class SocialAccount extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SocialCode socialCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public SocialAccount(String email, SocialCode socialCode, Member member) {
        this.email = email;
        this.socialCode = socialCode;
        this.member = member;
    }
}
