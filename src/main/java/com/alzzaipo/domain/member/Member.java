package com.alzzaipo.domain.member;

import com.alzzaipo.domain.BaseTimeEntity;
import com.alzzaipo.domain.kakaoAccountInfo.KakaoAccountInfo;
import com.alzzaipo.domain.notification.email.EmailNotification;
import com.alzzaipo.domain.notification.kakaotalk.KakaoTalkNotification;
import com.alzzaipo.domain.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false, name = "account_id")
    private String accountId;

    @Column(nullable = false, name = "account_password")
    private String accountPassword;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private EmailNotification emailNotification;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private KakaoTalkNotification kakaoTalkNotification;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private KakaoAccountInfo kakaoAccountInfo;

    @Builder
    public Member(String accountId, String accountPassword, String nickname, String email) {
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.nickname = nickname;
        this.email = email;
    }

    public void addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
        portfolio.setMember(this);
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
