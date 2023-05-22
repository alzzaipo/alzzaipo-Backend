package com.alzzaipo.domain.account.local;

import com.alzzaipo.domain.BaseTimeEntity;
import com.alzzaipo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class LocalAccount extends BaseTimeEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String accountId;

    @Column(nullable = false)
    private String accountPassword;

    @Column(unique = true, nullable = false, length = 256)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public LocalAccount(String accountId, String accountPassword, String email, Member member) {
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.email = email;
        this.member = member;
    }

    public void changeEmail(String email) {
        this.email = email;
    }
}
