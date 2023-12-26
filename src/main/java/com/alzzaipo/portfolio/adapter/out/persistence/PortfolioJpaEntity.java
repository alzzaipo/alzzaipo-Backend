package com.alzzaipo.portfolio.adapter.out.persistence;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.ipo.adapter.out.persistence.IpoJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "portfolio")
public class PortfolioJpaEntity extends BaseTimeEntity {

    @Id
    @Column(name = "portfolio_id", unique = true)
    private Long id;

    private int sharesCnt;

    private long profit;

    private long profitRate;

    private String agents;

    @Column(length = 200)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberJpaEntity memberJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipo_id")
    private IpoJpaEntity ipoJpaEntity;

    public PortfolioJpaEntity(Long id, int sharesCnt, long profit, long profitRate, String agents, String memo, MemberJpaEntity memberJpaEntity, IpoJpaEntity ipoJpaEntity) {
        this.id = id;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.profitRate = profitRate;
        this.agents = agents;
        this.memo = memo;
        this.memberJpaEntity = memberJpaEntity;
        this.ipoJpaEntity = ipoJpaEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
