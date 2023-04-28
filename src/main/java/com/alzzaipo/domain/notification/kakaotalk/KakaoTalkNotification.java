package com.alzzaipo.domain.notification.kakaotalk;

import com.alzzaipo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "kakaotalk_notification", uniqueConstraints = {
        @UniqueConstraint(name = "idx_phone", columnNames = {"country_code", "phone_number"})
})
public class KakaoTalkNotification {
    @Id @GeneratedValue
    @Column(name = "kakaotalk_notification_id")
    Long id;

    @Column(name = "country_code", nullable = false)
    String countryCode;

    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @Builder
    public KakaoTalkNotification(String countryCode, String phoneNumber, Member member) {
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.member = member;
    }
}
