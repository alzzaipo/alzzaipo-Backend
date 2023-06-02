package com.alzzaipo.domain.notification.criteria;

import com.alzzaipo.domain.member.Member;
import com.alzzaipo.dto.notification.NotificationCriteriaDto;
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

    @Column(name = "min_competition_rate", nullable = false,
            columnDefinition = "INT CHECK (min_competition_rate >= 0 AND min_competition_rate <= 10000)")
    private int minCompetitionRate;

    @Column(name = "min_lockup_rate", nullable = false,
            columnDefinition = "INT CHECK (min_lockup_rate >= 0 AND min_lockup_rate <= 100)")
    private int minLockupRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public NotificationCriteria(int minCompetitionRate, int minLockupRate, Member member) {
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;
        this.member = member;
    }

    public void update(int newCompetitionRate, int newLockupRate) {
        this.minCompetitionRate = newCompetitionRate;
        this.minLockupRate = newLockupRate;
    }

    public NotificationCriteriaDto toDto() {
        return new NotificationCriteriaDto(this.id, this.getMinCompetitionRate(), this.minLockupRate);
    }
}
