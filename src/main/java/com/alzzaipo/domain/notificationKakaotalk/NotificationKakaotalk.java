package com.alzzaipo.domain.notificationKakaotalk;

import com.alzzaipo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notification_kakaotalk", uniqueConstraints = {
        @UniqueConstraint(name = "idx_phone", columnNames = {"country_code", "phone_number"})
})
public class NotificationKakaotalk {
    @Id @GeneratedValue
    @Column(name = "notification_kakaotalk_id")
    Long id;

    @Column(name = "country_code", nullable = false)
    String countryCode;

    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @Builder
    public NotificationKakaotalk(String countryCode, String phoneNumber, Member member) {
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.member = member;
    }
}
