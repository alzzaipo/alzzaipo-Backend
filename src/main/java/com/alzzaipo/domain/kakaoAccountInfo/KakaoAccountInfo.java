package com.alzzaipo.domain.kakaoAccountInfo;

import com.alzzaipo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class KakaoAccountInfo {

    @Id @GeneratedValue
    @Column(name = "kakao_account_info_id")
    Long id;

    @Column(name = "kakao_account_id", nullable = false, unique = true)
    Long kakaoAccountId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @Builder
    public KakaoAccountInfo(Long kakaoAccountId, Member member) {
        this.kakaoAccountId = kakaoAccountId;
        this.member = member;
    }
}
