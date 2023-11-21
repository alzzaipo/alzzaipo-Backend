package com.alzzaipo.member.adapter.out.persistence.member;

import com.alzzaipo.common.BaseTimeEntity;
import com.alzzaipo.member.adapter.out.persistence.account.local.LocalAccountJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.account.social.SocialAccountJpaEntity;
import com.alzzaipo.notification.adapter.out.persistence.criterion.NotificationCriterionJpaEntity;
import com.alzzaipo.portfolio.adapter.out.persistence.PortfolioJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private List<SocialAccountJpaEntity> socialAccountJpaEntities = new ArrayList<>();

	@OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<PortfolioJpaEntity> portfolioJpaEntities = new ArrayList<>();

	@OneToMany(mappedBy = "memberJpaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<NotificationCriterionJpaEntity> notificationCriteriaJpaEntities = new ArrayList<>();

	public MemberJpaEntity(Long uid, String nickname) {
		this.uid = uid;
		this.nickname = nickname;
	}

	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}
}
