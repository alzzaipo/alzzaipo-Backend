package com.alzzaipo.hexagonal.member.adapter.out.persistence.member;

import com.alzzaipo.hexagonal.common.BaseTimeEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.account.local.LocalAccountJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.account.social.SocialAccountJpaEntity;
import com.alzzaipo.hexagonal.notification.adapter.out.persistence.criterion.NotificationCriterionJpaEntity;
import com.alzzaipo.hexagonal.portfolio.adapter.out.persistence.PortfolioJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class MemberJpaEntity extends BaseTimeEntity {

    @Id
    private Long uid;

    @Column(nullable = false)
    private String nickname;

    @OneToOne(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private LocalAccountJpaEntity localAccountJpaEntity;

    @OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SocialAccountJpaEntity> socialAccountJpaEntities;

    @OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PortfolioJpaEntity> portfolioJpaEntities;

    @OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NotificationCriterionJpaEntity> notificationCriteriaJpaEntities;

    public MemberJpaEntity(Long uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
