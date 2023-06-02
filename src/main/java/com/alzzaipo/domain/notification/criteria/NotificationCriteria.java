package com.alzzaipo.domain.notification.criteria;

import com.alzzaipo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class NotificationCriteria {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int minCompetitionRate;

    @Column(nullable = false)
    private int minLockupRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public NotificationCriteria(int minCompetitionRate, int minLockupRate, Member member) {
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;
        this.member = member;
    }
}
